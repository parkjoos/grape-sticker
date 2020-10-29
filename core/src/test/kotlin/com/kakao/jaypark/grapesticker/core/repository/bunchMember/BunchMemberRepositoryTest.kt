package com.kakao.jaypark.grapesticker.core.repository.bunchMember

import com.kakao.jaypark.grapesticker.core.CoreApplication
import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.BunchMember
import com.kakao.jaypark.grapesticker.core.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.repository.AbstractRepositoryTest
import com.kakao.jaypark.grapesticker.core.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

class BunchMemberRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var bunchRepository: BunchRepository

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var bunchMemberRepository: BunchMemberRepository

    @Test
    fun test() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        Assertions.assertThat(bunch).extracting("id").isNotNull()

        val member = Member(name = "jay.park", email = "jay.park@kakao.com")
        memberRepository.save(member)
        Assertions.assertThat(member).extracting("id").isNotNull()

        val bunchMember = BunchMember(BunchMemberKey(bunchId = bunch.id.orEmpty(), memberId = member.id.orEmpty()))
        bunchMemberRepository.save(bunchMember)

    }
}