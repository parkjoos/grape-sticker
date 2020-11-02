package com.kakao.jaypark.grapesticker.api.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(HomeController::class)
class HomeControllerTest(@Autowired val client: WebTestClient) {

    @Test
    fun aliveTest() {
        client.get().uri("/alive")
                .exchange()
                .expectStatus().isOk
    }
}