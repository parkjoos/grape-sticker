package net.jaypark.grapesticker.repository

import net.jaypark.grapesticker.domain.BunchMember
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface BunchMemberRepository : CrudRepository<BunchMember, String> {
    fun findAllByMemberId(memberId: String): Set<BunchMember>
    fun findByBunchId(bunchId: String): Set<BunchMember>
    fun findByBunchIdAndMemberId(bunchId: String, memberId: String): BunchMember?
}