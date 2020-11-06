package com.kakao.jaypark.grapesticker.service

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.BunchMember
import com.kakao.jaypark.grapesticker.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doAnswer
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing
import java.util.*

@ExtendWith(MockitoExtension::class)
class BunchServiceTest {

    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var bunchRepository: BunchRepository

    @Mock
    private lateinit var bunchMemberRepository: BunchMemberRepository

    @InjectMocks
    private lateinit var bunchService: BunchService

    @Test
    fun testCreateBunch(){
        val bunch = Bunch(name = "테스트용 포토송이")
        val member = Member(id="memberidtest-34fw34-w3g",email = "jaypark@kakao.com",name="jaypark")

        doAnswer {
            val mockBunch = it.arguments[0] as Bunch
            mockBunch.id = "test-bucnhid-w3gfwe5t"
            mockBunch
        }.`when`(bunchRepository).save(ArgumentMatchers.any())

        bunchService.create(bunch, member)
    }

    @Test
    fun testCreateBunchNameValidationFail(){
        val bunch = Bunch()
        val member = Member(id="memberidtest-34fw34-w3g",email = "jaypark@kakao.com",name="jaypark")

        assertThatThrownBy {
            bunchService.create(bunch, member)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("bunch name is required")
    }

    @Test
    fun testGetBunch(){
        val testBunchId = "3w456tqew65-3456wsergt-23456"

        val expected = Bunch(id = testBunchId, name = "test bunch")
        whenever(bunchRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(expected))

        val result = bunchService.get(testBunchId)
        assertThat(result).extracting("id").isEqualTo(testBunchId);
    }

    @Test
    fun testGetBunchNotFound(){
        val testBunchId = "3w456tqew65-3456wsergt-23456"
        whenever(bunchRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty())
        assertThatThrownBy {
            bunchService.get(testBunchId)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("can not find bunch")
    }

    @Test
    fun testNameDuplicated(){
        val bunch = Bunch(name = "테스트용 포토송이")
        val member = Member(id="memberidtest-34fw34-w3g",email = "jaypark@kakao.com",name="jaypark")

        whenever(bunchMemberRepository.findAllByMemberId(member.id!!))
                .thenReturn(hashSetOf(BunchMember(BunchMemberKey(bunchId = "randombunchdf-43gae4-a4ef", memberId = member.id!!))))

        whenever(bunchRepository.findAllByIdIn(hashSetOf("randombunchdf-43gae4-a4ef")))
                .thenReturn(hashSetOf(bunch))

        assertThatThrownBy {
            bunchService.create(bunch, member)
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("bunch name duplicated")
    }

    @Test
    fun testDeleteBunch() {
        val bunch = Bunch(id = "234tf2356-se67t76-2134r13t", name = "테스트용 포토송이")
        whenever(bunchRepository.findById(bunch.id!!)).thenReturn(Optional.of(bunch))
        bunchService.delete(bunch)

    }

    @Test
    fun testModifyBunch() {
        val bunch = Bunch(id = "234tf2356-se67t76-2134r13t", name = "테스트용 포토송이", maxNumberOfGrapes = 10)
        whenever(bunchRepository.findById(bunch.id!!)).thenReturn(Optional.of(bunch))
        bunchService.modify(bunch.copy(name = "alter name", maxNumberOfGrapes = 15))
        assertThat(bunch.name).isEqualTo("alter name")
        assertThat(bunch.maxNumberOfGrapes).isEqualTo(15)
    }
}