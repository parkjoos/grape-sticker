package com.kakao.jaypark.grapesticker.service

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.BunchMember
import com.kakao.jaypark.grapesticker.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.repository.BunchRepository
import org.springframework.stereotype.Service

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
        return bunchRepository.findAllByIdIn(buncheMembers
                .map { it.getBunchId() }.toSet())
    }

    fun delete(bunch: Bunch) {
        val target = get(bunchId = bunch.id!!)
        bunchRepository.delete(target)
    }

    fun modify(bunchToModify: Bunch) {
        val target = get(bunchId = bunchToModify.id!!)
        target.modify(bunchToModify)
        bunchRepository.save(target)
    }

}
