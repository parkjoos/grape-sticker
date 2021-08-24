import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	base
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.4.31"
    kotlin("plugin.spring") version "1.4.31"
}

allprojects {
    group = "net.jaypark"
    version = "0.0.1-SNAPSHOT"
    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://s3.ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/release") // for DynamoDBLocal Lib
    }
}

java.sourceCompatibility = JavaVersion.VERSION_11

dependencies {
	subprojects.forEach {
		archives(it)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}