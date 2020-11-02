package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.domain.enums.GrapeStickerType
import com.kakao.jaypark.grapesticker.service.BunchService
import com.kakao.jaypark.grapesticker.service.MemberService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ExtendWith(SpringExtension::class)
@WebFluxTest(BunchController::class)
class BunchControllerTest(@Autowired val client: WebTestClient) {

    @MockBean
    lateinit var bunchService: BunchService

    @MockBean
    lateinit var memberService: MemberService

    @Test
    fun testCreate() {
        client.post().uri("/bunches")
                .bodyValue(BunchTO(name = "new grape bunch1", maxNumberOfGrapes = 15, stickerType = GrapeStickerType.PRAISE))
                .exchange()
                .expectStatus().isOk

    }
}