package com.kakao.jaypark.grapesticker.core.repository.member

import com.kakao.jaypark.grapesticker.core.CoreApplication
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CoreApplication::class])
@ActiveProfiles("test")
class MemberRepositoryTest() {

    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun test() {
        val member = Member(name = "jay.park", email = "jay.park@kakao.com")
        memberRepository.save(member)
        assertThat(member).extracting("id").isNotNull()
    }
}