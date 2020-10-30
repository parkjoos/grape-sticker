package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.Grape
import com.kakao.jaypark.grapesticker.core.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing

@ExtendWith(MockitoExtension::class)
class GrapeStickerServiceTest {
    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var bunchRepository: BunchRepository

    @Mock
    private lateinit var bunchMemberRepository: BunchMemberRepository

    @InjectMocks
    private lateinit var grapeStickerService: GrapeStickerService

    @Test
    fun testAttachSticker(){
        val bunch = Bunch(id="testbundch-se4agf-weg-ae4tg", name = "testbunch")
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        grapeStickerService.attach(bunch, grape)
    }

    @Test
    fun testAttachGrapeAtDuplicatedPosition(){
        assertThatThrownBy {
            val bunch = Bunch(id="testbundch-se4agf-weg-ae4tg", name = "testbunch")
            val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape)

            val grape2 = Grape(position = 5, comment = "sicker2", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape2)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("grape position duplicated")
    }
}