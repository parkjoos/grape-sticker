package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.BunchMember
import com.kakao.jaypark.grapesticker.core.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class BunchService(
        var bunchRepository: BunchRepository,
        var bunchMemberRepository: BunchMemberRepository
) {
    fun create(bunch: Bunch, member: Member) {
        validate(bunch, member)
        bunchRepository.save(bunch)
        bunchMemberRepository.save(BunchMember(BunchMemberKey(bunchId = bunch.id!!, memberId = member.id!!)))
    }

    private fun validate(requestedBunch: Bunch, member: Member) {
        requestedBunch.name ?: throw RuntimeException("bunch name is required")

        val allBunchesByMember = getAllBunchesByMember(member)
        val nameDuplicate = allBunchesByMember.stream()
                .anyMatch { it.name.equals(requestedBunch.name) }

        if (nameDuplicate) {
            throw RuntimeException("bunch name duplicated")
        }
    }

    fun get(bunchId: String): Bunch {
        return bunchRepository.findById(bunchId).orElseThrow { RuntimeException("can not find bunch") }
    }

    fun getAllBunchesByMember(member: Member): Set<Bunch> {
        val buncheMembers: Set<BunchMember> = bunchMemberRepository.findAllByMemberId(member.id!!)
        return bunchRepository.findAllByIdIn(buncheMembers.stream()
                .map { it.getBunchId() }
                .collect(Collectors.toSet()))
    }

}
