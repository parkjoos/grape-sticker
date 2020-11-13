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
import java.time.LocalDateTime

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
    fun testAttachSticker() {
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", maxNumberOfGrapes = 2)
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        grapeStickerService.attach(bunch, grape, Member(id = "tests4-w4ts4t-s4w5s", email = "jay.park@kakao.com", name = "jay.park"))
        assertThat(grape.createdDate).isNotNull
        assertThat(grape.lastModifiedDate).isNotNull

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
        grapeStickerService.remove(bunch, 5)

        assertThat(bunch.grapes).doesNotContain(grape)
    }

    @Test
    fun testRemoveStickerAtNotExistsPosition() {
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", grapes = hashSetOf(grape))

        assertThatThrownBy {
            grapeStickerService.remove(bunch, 2)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining("no grape at position")

    }

    @Test
    fun testModifyGrape() {
        val yesterday = LocalDateTime.now().minusDays(1)
        val grape = Grape(position = 5, comment = "sicker1", writerId = "ememberis32-345g245-45g", lastModifiedDate = yesterday)
        val grapeToModify = Grape(position = 5, comment = "sicker2", writerId = "3456y34567y-345g245-45g")
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", grapes = hashSetOf(grape))

        grapeStickerService.modify(bunch, grapeToModify)

        assertThat(bunch.grapes).anyMatch {
            it.position == grapeToModify.position
        }.allSatisfy {
            assertThat(it.comment).isEqualTo(grapeToModify.comment)
            assertThat(it.writerId).isEqualTo(grapeToModify.writerId)
            assertThat(it.lastModifiedDate).isNotEqualTo(yesterday)
        }
    }

    @Test
    fun testModifyGrapeIllegalPosition() {
        val grape = Grape(position = 1, comment = "sicker1", writerId = "ememberis32-345g245-45g")
        val grapeToModify = Grape(position = 5, comment = "sicker2", writerId = "ememberis32-345g245-45g")
        val bunch = Bunch(id = "testbundch-se4agf-weg-ae4tg", name = "testbunch", grapes = hashSetOf(grape))

        assertThatThrownBy {
            grape.modify(grapeToModify)
        }.isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("can modify only same position")

        assertThatThrownBy {
            grapeStickerService.modify(bunch, grapeToModify)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("no grape at position")

    }

}