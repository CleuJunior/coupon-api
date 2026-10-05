# Coupon API

API REST para cadastro e remoção de cupons de desconto, construída como desafio técnico com foco em **regras de negócio bem encapsuladas** (DDD) e **separação de camadas** (arquitetura hexagonal / ports & adapters).

O desafio não é sobre ter muitos endpoints — é sobre `Create` e `Delete` funcionando corretamente, respeitando todas as regras de negócio, com a lógica de domínio isolada de frameworks e banco de dados.

## Stack

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Validation)
- H2 (banco em memória)
- springdoc-openapi (Swagger UI)
- Gradle (Kotlin DSL)
- JUnit 5, Mockito, AssertJ (testes unitários)
- RestAssured (testes de integração)
- Docker / Docker Compose

## Arquitetura

O projeto segue **arquitetura hexagonal** com as regras de negócio encapsuladas em objetos de domínio (nunca em `if/else` espalhado em services genéricos):

```
src/main/java/br/com/coupon/
├── domain/              # Regras de negócio puras. Zero dependência de Spring/JPA.
│   ├── Coupon.java          # Entidade de domínio: validações de criação e soft delete
│   ├── CouponCode.java      # Value object: sanitização e validação do código (6 chars)
│   └── exception/           # Exceptions de domínio (CouponCreationException e subtipos, etc.)
│
├── application/         # Orquestração (casos de uso). Depende só de interfaces (ports).
│   ├── port/                # CouponRepositoryPort — a "porta" pro mundo externo
│   └── usecase/              # CreateCouponUseCase, DeleteCouponUseCase — um método público cada
│
└── infra/               # Adapters. Tudo que fala com o mundo externo mora aqui.
    ├── web/                  # Controller, DTOs, mapper web, exception handler global
    ├── persistence/          # Entity JPA, repository Spring Data, adapter que implementa o port
    └── config/               # Beans de configuração (use cases, OpenAPI)
```

Regra seguida à risca: a camada `application` não importa nada de `org.springframework.web` ou `jakarta.persistence` — ela só conhece `CouponRepositoryPort` (interface). Quem implementa a interface e fala com o JPA é o `infra.persistence.adapter.CouponRepositoryAdapter`.

## Regras de negócio

**Criação (`POST /api/v1/coupon`)**
- Campos obrigatórios: `code`, `description`, `discountValue`, `expirationDate`
- `code` é sanitizado (caracteres especiais removidos) e precisa resultar em exatamente **6 caracteres alfanuméricos** — senão é rejeitado
- `discountValue` tem saldo mínimo de **0.5**, sem máximo
- `expirationDate` nunca pode estar no passado
- Um cupom pode ser criado já como `published: true`

**Remoção (`DELETE /api/v1/coupon/{id}`)**
- Soft delete — o registro nunca é apagado, só marcado com `deletedAt`
- Não é possível deletar um cupom que já foi deletado (retorna `409 Conflict`)

## Como rodar localmente

Pré-requisito: JDK 21 (o Gradle wrapper cuida do resto).

```bash
./gradlew bootRun
```

A API sobe em `http://localhost:8080`.

## Rodando com Docker

```bash
docker compose up --build
```

Isso builda a imagem (multi-stage: compila com JDK, roda com JRE) e sobe o container expondo a porta `8080`. Pra derrubar:

```bash
docker compose down
```

> O H2 Console está habilitado também dentro do container (`spring.h2.console.settings.web-allow-others: true`) — sem essa flag, o H2 bloqueia conexões que não pareçam vir de `127.0.0.1`, o que é sempre o caso quando a requisição atravessa a rede do Docker. Isso é uma configuração de conveniência para desenvolvimento/avaliação do desafio — não deveria ir pra um ambiente de produção real sem estar atrás de uma rede restrita.

## Dados de exemplo (seed)

Ao subir, a aplicação popula o H2 automaticamente com **10 cupons de exemplo** (`src/main/resources/data.sql`) — alguns publicados, alguns não, e dois já soft-deletados (ids `9` e `10`), úteis pra testar o cenário de conflito (`409`) sem precisar criar dado na mão.

## Acessando a aplicação

| Recurso | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| H2 Console | http://localhost:8080/h2-console |

Credenciais do H2 Console:
- **JDBC URL**: `jdbc:h2:mem:coupondb`
- **User**: `sa`
- **Password**: *(em branco)*

### Endpoints

| Método | Rota | Descrição | Sucesso | Erros possíveis |
|---|---|---|---|---|
| `POST` | `/api/v1/coupon` | Cria um cupom | `201 Created` | `400` (regra de negócio violada ou JSON inválido) |
| `DELETE` | `/api/v1/coupon/{id}` | Remove (soft delete) um cupom | `204 No Content` | `404` (não existe), `409` (já deletado) |

Exemplo de criação:

```bash
curl -X POST http://localhost:8080/api/v1/coupon \
  -H "Content-Type: application/json" \
  -d '{
        "code": "AB-12#34",
        "description": "10% de desconto",
        "discountValue": 10.00,
        "expirationDate": "2026-12-31",
        "published": false
      }'
```

O `code` acima vira `AB1234` na resposta (caracteres especiais removidos antes de salvar).

### Formato de erro

Toda resposta de erro segue o mesmo formato, produzido pelo `GlobalExceptionHandler`:

```json
{
  "timestamp": "2026-10-05T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Discount value must be at least 0.5, got: '0.1'"
}
```

## Configuração

Valores como título/descrição/versão do Swagger não ficam hardcoded no código — vêm do `src/main/resources/application.yaml` via um `record` com `@ConfigurationProperties` (`OpenApiProperties`), seguindo o mesmo padrão pra qualquer configuração futura:

```yaml
coupon:
  openapi:
    title: "Coupon API"
    description: "API para cadastro e remoção de cupons de desconto"
    version: "v1"
```

## Testes

O projeto tem **dois níveis de teste**, em source sets Gradle separados, cada um com seu propósito:

### Testes unitários (`src/test`)

Cobrem as regras de negócio isoladamente — domínio (`Coupon`, `CouponCode`) e use cases (`CreateCouponUseCase`, `DeleteCouponUseCase`) com o repositório mockado. Rápidos, não sobem o contexto Spring nem servidor HTTP.

```bash
./gradlew test
```

### Testes de integração (`src/integrationTest`)

Sobem a aplicação inteira (`@SpringBootTest` com porta aleatória) e batem nos endpoints de verdade via **RestAssured**, validando os status HTTP do `GlobalExceptionHandler` ponta a ponta (criação com sucesso, validação, conflito de delete duplicado, 404, JSON malformado etc). A massa de dados de teste é montada com o padrão **Fixture** (`fixture/CouponRequestFixture.java`), evitando repetir a construção de `CreateCouponRequest` em cada cenário.

```bash
./gradlew integrationTest
```

### Rodando tudo

```bash
./gradlew check
```

`check` é a task de verificação padrão do Gradle — ela já está configurada pra rodar `test` e `integrationTest` em sequência. É o mesmo comando que roda num CI.

## Decisões técnicas relevantes

- **`CreateCouponUseCase` e `DeleteCouponUseCase` fazem uma coisa só** — nada de `CouponService` genérico com vários métodos. Cada caso de uso é uma classe com um único método público (`execute`).
- **Regras de negócio vivem no domínio, não no use case.** `Coupon.of(...)` e `CouponCode.of(...)` são os únicos lugares que decidem se um cupom é válido — o use case só orquestra (busca, chama o domínio, persiste).
- **Soft delete** é responsabilidade do próprio `Coupon` (`coupon.delete()`), não uma flag manipulada de fora.
