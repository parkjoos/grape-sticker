package com.kakao.jaypark.grapesticker.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication(scanBasePackages = ["com.kakao.jaypark.grapesticker"])
@EnableWebFlux
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}
