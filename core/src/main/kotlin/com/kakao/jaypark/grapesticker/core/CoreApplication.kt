package com.kakao.jaypark.grapesticker.core

import com.kakao.jaypark.grapesticker.core.config.DynamoDBRepositoryConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(DynamoDBRepositoryConfiguration::class)
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
