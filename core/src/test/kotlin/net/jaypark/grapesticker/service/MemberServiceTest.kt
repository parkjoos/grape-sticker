package net.jaypark.grapesticker.service

import net.jaypark.grapesticker.domain.Bunch
import net.jaypark.grapesticker.domain.BunchMember
import net.jaypark.grapesticker.domain.BunchMemberKey
import net.jaypark.grapesticker.domain.Member
import net.jaypark.grapesticker.domain.enums.MemberStatus
import net.jaypark.grapesticker.domain.enums.MemberType
import net.jaypark.grapesticker.repository.BunchMemberRepository
import net.jaypark.grapesticker.repository.MemberRepository
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
import java.util.*

@ExtendWith(MockitoExtension::class)
class MemberServiceTest {

    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var memberRepository: MemberRepository

    @Mock
    private lateinit var bunchService: BunchService

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

        whenever(bunchService.get("bunch11-345g-34g"))
                .thenReturn(Bunch(id = "bunch11-345g-34g", name = "tstsebunch"))
        whenever(bunchService.get("bunch2-324f-34f"))
                .thenReturn(Bunch(id = "bunch2-324f-34f", name = "tstsebunch"))

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
    fun testMakeMasterWhoAreNotAMember() {
        assertThatThrownBy {
            memberService.makeMaster(Bunch(id = "345y673467y-aw45rhj587-3t2345t"), Member(id = "2314t45gt-aw46se576-days10"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("not a bunch member")
    }

    @Test
    fun testMakeMasterWhoAreAlreafyMaster() {
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("bunch-34f-34fwe4", "member1-a34f-34f34-34f"))
                .thenReturn(BunchMember(BunchMemberKey(bunchId = "bunch-34f-34fwe4", memberId = "member1-a34f-34f34-34f"), type = MemberType.MASTER))
        assertThatThrownBy {
            memberService.makeMaster(Bunch(id = "bunch-34f-34fwe4"), Member(id = "member1-a34f-34f34-34f"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("already Master")
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
    fun testAddMemberWhoAlreadyMember() {
        whenever(memberRepository.findById("34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(Optional.of(Member(id = "34faefa-ew5gfdg45fg-34tasrt")))
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(BunchMember(BunchMemberKey("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt")))
        assertThatThrownBy {
            memberService.addToBunch(Bunch(id = "34f34f-f34f43-34f34f"), Member(id = "34faefa-ew5gfdg45fg-34tasrt"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("already member")
    }

    @Test
    fun testRemoveMember() {
        whenever(memberRepository.findById("34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(Optional.of(Member(id = "34faefa-ew5gfdg45fg-34tasrt")))
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(BunchMember(BunchMemberKey("34f34f-f34f43-34f34f", "34faefa-ew5gfdg45fg-34tasrt")))
        memberService.removeFromBunch(Bunch(id = "34f34f-f34f43-34f34f"), Member(id = "34faefa-ew5gfdg45fg-34tasrt"))
    }

    @Test
    fun testRemoveMemberWhoAreNotAMember() {
        whenever(memberRepository.findById("34faefa-ew5gfdg45fg-34tasrt"))
                .thenReturn(Optional.of(Member(id = "34faefa-ew5gfdg45fg-34tasrt")))
        assertThatThrownBy {
            memberService.removeFromBunch(Bunch(id = "34f34f-f34f43-34f34f"), Member(id = "34faefa-ew5gfdg45fg-34tasrt"))
        }.isInstanceOf(RuntimeException::class.java)
                .hasMessage("already removed")

    }

    @Test
    fun testGetMembersMap() {
        val ids = hashSetOf("34faefa-ew5gfdg45fg-34tasrt", "3465745h56-356g356y-3456hg3456y", "345t345t-34g63425g25-b567zdt")
        whenever(memberRepository.findByIdIn(ids))
                .thenReturn(hashSetOf(Member(id = "34faefa-ew5gfdg45fg-34tasrt"), Member(id = "3465745h56-356g356y-3456hg3456y"), Member(id = "345t345t-34g63425g25-b567zdt")))
        val map = memberService.getMap(ids)
        assertThat(map).allSatisfy { id, member ->
            assertThat(id).isIn(ids)
            assertThat(member.id).isIn(ids)
        }
    }

    @Test
    fun testElectMasterToOldestMember() {
        var bunch = Bunch(id = "2354t-245t245t-245tg245t", name = "test bunch")
        whenever(bunchMemberRepository.findByBunchId("2354t-245t245t-245tg245t"))
                .thenReturn(hashSetOf(BunchMember(key = BunchMemberKey("2354t-245t245t-245tg245t", "2314t45gt-aw46se576-days10"), createdDate = LocalDateTime.now().minusDays(10), type = MemberType.MEMBER),
                        BunchMember(key = BunchMemberKey("2354t-245t245t-245tg245t", "4de56fjh5-e4g3456-days9"), createdDate = LocalDateTime.now().minusDays(9), type = MemberType.MEMBER),
                        BunchMember(key = BunchMemberKey("2354t-245t245t-245tg245t", "456d35j4r-e5j8w342-days8"), createdDate = LocalDateTime.now().minusDays(8), type = MemberType.MEMBER),
                        BunchMember(key = BunchMemberKey("2354t-245t245t-245tg245t", "dxfh45gh2-234h7563-days7"), createdDate = LocalDateTime.now().minusDays(8), type = MemberType.MEMBER)))
        val expectedBunchMember = BunchMember(key = BunchMemberKey("2354t-245t245t-245tg245t", "2314t45gt-aw46se576-days10"), createdDate = LocalDateTime.now().minusDays(10), type = MemberType.MEMBER)
        whenever(bunchMemberRepository.findByBunchIdAndMemberId("2354t-245t245t-245tg245t", "2314t45gt-aw46se576-days10"))
                .thenReturn(expectedBunchMember)
        whenever(memberRepository.findById("2314t45gt-aw46se576-days10")).thenReturn(Optional.of(Member(id = "2314t45gt-aw46se576-days10")))
        memberService.electMaster(bunch)

        assertThat(expectedBunchMember.type).isEqualTo(MemberType.MASTER)
    }
}