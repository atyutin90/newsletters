plugins {
	id("org.springframework.boot") version "4.1.1"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "2.4.20"
	kotlin("plugin.spring") version "2.4.20"
	kotlin("plugin.jpa") version "2.4.20"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
	maven { url = uri("https://jaspersoft.jfrog.io/artifactory/third-party-ce-artifacts/") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("de.phip1611:docx4j-search-and-replace-util:2.0.0")
	implementation("org.freemarker:freemarker:2.3.33")
	implementation("org.reflections:reflections:0.10.2")
	implementation("no.api.freemarker:freemarker-java8:3.0.0")
	implementation("net.sf.jasperreports:jasperreports:6.20.6")
	implementation("org.apache.poi:poi-ooxml:5.5.1")
	implementation("io.github.oshai:kotlin-logging:7.0.0")
	runtimeOnly("io.github.oshai:kotlin-logging-jvm:7.0.0")
	runtimeOnly("org.postgresql:postgresql")
	runtimeOnly("org.flywaydb:flyway-core:9.22.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.test {
	useJUnitPlatform()
}

tasks.processResources {
	from("report") {
		into("report")
	}
}
