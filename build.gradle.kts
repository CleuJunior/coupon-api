plugins {
	java
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "br.com.coupon"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

sourceSets {
	create("integrationTest") {
		java.srcDir("src/integrationTest/java")
		resources.srcDir("src/integrationTest/resources")
		compileClasspath += sourceSets.main.get().output + sourceSets.test.get().output
		runtimeClasspath += sourceSets.main.get().output + sourceSets.test.get().output
	}
}

configurations.getByName("integrationTestImplementation") {
	extendsFrom(configurations.getByName("testImplementation"))
}
configurations.getByName("integrationTestRuntimeOnly") {
	extendsFrom(configurations.getByName("testRuntimeOnly"))
}
configurations.getByName("integrationTestAnnotationProcessor") {
	extendsFrom(configurations.getByName("testAnnotationProcessor"))
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-h2console")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-validation:4.1.1")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.3")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testCompileOnly("org.projectlombok:lombok")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testAnnotationProcessor("org.projectlombok:lombok")
	"integrationTestImplementation"("io.rest-assured:rest-assured:6.0.1")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val integrationTest = tasks.register<Test>("integrationTest") {
	description = "Runs integration tests."
	group = "verification"
	testClassesDirs = sourceSets["integrationTest"].output.classesDirs
	classpath = sourceSets["integrationTest"].runtimeClasspath
	shouldRunAfter(tasks.test)
}

tasks.check {
	dependsOn(integrationTest)
}