import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	base
	kotlin("jvm") version "1.3.72"
	kotlin("plugin.spring") version "1.3.72"
}

allprojects {
	group = "com.kakao.jaypark"
	version = "0.0.1-SNAPSHOT"
	repositories {
		mavenCentral()
		maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
		maven(url = "https://s3.ap-northeast-1.amazonaws.com/dynamodb-local-tokyo/release") // for DynamoDBLocal Lib
	}
}

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