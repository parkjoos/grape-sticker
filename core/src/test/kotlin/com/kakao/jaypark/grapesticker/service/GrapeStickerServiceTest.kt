package com.kakao.jaypark.grapesticker.service

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.Grape
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
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
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", maxNumberOfGrapes = 2)
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        grapeStickerService.attach(bunch, grape, Member(id="tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com",name="jay.park"))
    }

    @Test
    fun testAttachGrapeAtDuplicatedPosition(){
        assertThatThrownBy {
            val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", maxNumberOfGrapes = 2)
            val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape, Member(id = "tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com", name = "jay.park"))

            val grape2 = Grape(position = 5, comment = "sicker2", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape2, Member(id = "tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com", name = "jay.park"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("grape position duplicated")
    }

    @Test
    fun testAttachGrapeOverMaxNumberOfGrapes() {
        assertThatThrownBy {
            val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", maxNumberOfGrapes = 1)
            val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape, Member(id = "tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com", name = "jay.park"))

            val grape2 = Grape(position = 5, comment = "sicker2", writerId = "ememberis32-345g245-45g")
            grapeStickerService.attach(bunch, grape2, Member(id = "tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com", name = "jay.park"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("max number of grapes exceeded")
    }

    @Test
    fun testRemoveSticker() {
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", grapes = hashSetOf(grape))
        grapeStickerService.remove(bunch, grape)

        assertThat(bunch.grapes).doesNotContain(grape)
    }
}