package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing

@ExtendWith(MockitoExtension::class)
class MemberServiceTest {

    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var memberRepository: MemberRepository

    @InjectMocks
    private lateinit var memberService: MemberService

    @Test
    fun testJoin(){
        memberService.join(Member(email = "jay.park@kakao.com"))
    }

    @Test
    fun testJoinAlreadyJoined() {
        whenever(memberRepository.findByEmail("joined@kakao.com"))
                .thenReturn(hashSetOf(Member(status = MemberStatus.VALID, email = "joined@kakao.com")))
        assertThatThrownBy {
            memberService.join(Member(email = "joined@kakao.com"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining("already joined")

        whenever(memberRepository.findByEmail("pending@kakao.com"))
                .thenReturn(hashSetOf(Member(status = MemberStatus.PENDING, email = "pending@kakao.com")))
        assertThatThrownBy {
            memberService.join(Member(email = "pending@kakao.com"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessageContaining("pending")

    }

    @Test
    fun testWithdrawal(){
        memberService.withdrawal(Member(id="q34faw4-435g245g-345g345g"))
    }
}