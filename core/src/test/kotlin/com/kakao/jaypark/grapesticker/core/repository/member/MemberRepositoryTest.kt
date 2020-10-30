package com.kakao.jaypark.grapesticker.core.repository.member

import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.repository.AbstractRepositoryTest
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MemberRepositoryTest : AbstractRepositoryTest(){

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun test() {
        val member = Member(name = "jay.park", email = "jay.park@kakao.com")
        memberRepository.save(member)
        assertThat(member).extracting("id").isNotNull()
    }

    @Test
    fun testFindByEmail(){
        memberRepository.save(Member(name = "jay.park", email = "jay.park@kakao.com"))
        memberRepository.save(Member(name = "jay.park1", email = "jay.park1@kakao.com"))

        val result = memberRepository.findByEmail("jay.park@kakao.com")
        assertThat(result).allSatisfy {
            assertThat(it.name).isEqualTo("jay.park")
        }

    }
}