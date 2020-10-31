package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.BunchMember
import com.kakao.jaypark.grapesticker.core.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.core.domain.enums.MemberType
import com.kakao.jaypark.grapesticker.core.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing
import java.util.*
import kotlin.collections.HashSet

@ExtendWith(MockitoExtension::class)
class MemberServiceTest {

    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var memberRepository: MemberRepository

    @Mock
    private lateinit var bunchRepository: BunchRepository

    @Mock
    private lateinit var bunchMemberRepository: BunchMemberRepository

    @InjectMocks
    private lateinit var memberService: MemberService

    @Test
    fun testJoin() {
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
    fun testWithdrawal() {
        whenever(memberRepository.findById("q34faw4-435g245g-345g345g"))
                .thenReturn(Optional.of(Member(status = MemberStatus.VALID, email = "joined@kakao.com")))
        whenever(bunchMemberRepository.findAllByMemberId("q34faw4-435g245g-345g345g"))
                .thenReturn(hashSetOf(BunchMember(BunchMemberKey(bunchId = "bunch11-345g-34g", memberId = "q34faw4-435g245g-345g345g")),
                        BunchMember(BunchMemberKey(bunchId = "bunch2-324f-34f", memberId = "q34faw4-435g245g-345g345g"))))

        whenever(bunchMemberRepository.findByBunchId("bunch11-345g-34g"))
                .thenReturn(hashSetOf(BunchMember(BunchMemberKey(bunchId = "bunch11-345g-34g", memberId = "w45gw4g-se5yse5-gd6356g")),
                        BunchMember(BunchMemberKey(bunchId = "bunch11-345g-34g", memberId = "43ge4653-sde5tse5t-g345ghyrh"))))
        whenever(bunchMemberRepository.findByBunchId("bunch2-324f-34f")).thenReturn(HashSet())

        whenever(bunchRepository.findById("bunch11-345g-34g"))
                .thenReturn(Optional.of(Bunch(id = "bunch11-345g-34g", name = "tstsebunch")))
        whenever(bunchRepository.findById("bunch2-324f-34f"))
                .thenReturn(Optional.of(Bunch(id = "bunch2-324f-34f", name = "tstsebunch")))

        memberService.withdrawal(Member(id = "q34faw4-435g245g-345g345g"))
    }

    @Test
    fun getOneByEmailTest() {
        whenever(memberRepository.findByEmail("jay.park@kakao.com"))
                .thenReturn(hashSetOf(Member(email = "jay.park@kakao.com", name = "test1"),
                        Member(email = "jay.park@kakao.com", name = "test2")))
        val result = memberService.getOneByEmail("jay.park@kakao.com")

        assertThat(result!!).isInstanceOf(Member::class.java)
    }

    @Test
    fun getBunchMembersTest() {
        whenever(bunchMemberRepository.findByBunchId("bunch1-234rf34-324f234r"))
                .thenReturn(hashSetOf(BunchMember(BunchMemberKey(bunchId = "bunch1-234rf34-324f234r", memberId = "w45gw4g-se5yse5-gd6356g")),
                        BunchMember(BunchMemberKey(bunchId = "bunch1-234rf34-324f234r", memberId = "43ge4653-sde5tse5t-g345ghyrh"))))
        whenever(memberRepository.findByIdIn(hashSetOf("w45gw4g-se5yse5-gd6356g", "43ge4653-sde5tse5t-g345ghyrh")))
                .thenReturn(hashSetOf(Member(id = "43ge4653-sde5tse5t-g345ghyrh"), Member(id = "w45gw4g-se5yse5-gd6356g")))
        val bunchMembers = memberService.getBunchMembers(Bunch(id = "bunch1-234rf34-324f234r"))
        assertThat(bunchMembers).extracting("id").contains("43ge4653-sde5tse5t-g345ghyrh")
        assertThat(bunchMembers).extracting("id").contains("w45gw4g-se5yse5-gd6356g")
    }

    @Test
    fun modifyMemberTest() {
        whenever(memberRepository.findById("test-member-sw34f"))
                .thenReturn(Optional.of(Member(status = MemberStatus.VALID, email = "joined@kakao.com")))
        memberService.modifyName("test-member-sw34f", "newName")
    }

    @Test
    fun testMakeMaster() {
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("bunch-34f-34fwe4", "member1-a34f-34f34-34f"))
                .thenReturn(BunchMember(BunchMemberKey(bunchId = "bunch-34f-34fwe4", memberId = "member1-a34f-34f34-34f"), type = MemberType.MEMBER))
        memberService.makeMaster(Bunch(id = "bunch-34f-34fwe4"), Member(id = "member1-a34f-34f34-34f"))
    }

    @Test
    fun testAddMember() {
        whenever(memberRepository.findById("34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(Optional.of(Member(id = "34faefa-ew5gfdg45fg-34tasrt")))
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(null)
        memberService.addToBunch(Bunch(id = "34f34f-f34f43-34f34f"), Member(id = "34faefa-ew5gfdg45fg-34tasrt"))
    }

    @Test
    fun testRemoveMember() {
        whenever(memberRepository.findById("34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(Optional.of(Member(id = "34faefa-ew5gfdg45fg-34tasrt")))
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(BunchMember(BunchMemberKey("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt")))
        memberService.removeFromBunch(Bunch(id = "34f34f-f34f43-34f34f"), Member(id = "34faefa-ew5gfdg45fg-34tasrt"))
    }
}