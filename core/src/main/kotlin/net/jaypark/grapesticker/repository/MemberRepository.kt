package net.jaypark.grapesticker.repository

import net.jaypark.grapesticker.domain.Member
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface MemberRepository : CrudRepository<Member, String> {
    fun findByEmail(email: String): Set<Member>
    fun findByOAuth2Id(oAuth2Id: String): Set<Member>
    fun findByIdIn(memberIds: Set<String>): Set<Member>
}