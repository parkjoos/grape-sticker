package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.core.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import com.kakao.jaypark.grapesticker.core.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
        private var memberRepository: MemberRepository,
        private var bunchMemberRepository: BunchMemberRepository,
        private var bunchRepository: BunchRepository
) {
    fun join(member: Member) {
        memberRepository.findByEmail(member.email!!).forEach {
            var message = "already joined email"
            if (it.status == MemberStatus.PENDING) {
                message = "email pending activation, try to activate your email"
            }
            throw RuntimeException(message)
        }
        memberRepository.save(member)
    }

    fun withdrawal(member: Member) {
        memberRepository.findById(member.id!!)
                .orElseThrow { RuntimeException("member not exists") }

        val bunchesByMember = bunchMemberRepository.findAllByMemberId(member.id!!)
        bunchesByMember.forEach {
            bunchMemberRepository.delete(it)
            val bunchMembers = bunchMemberRepository.findByBunchId(it.getBunchId())
            if (bunchMembers.isEmpty()) {
                val bunch = bunchRepository.findById(it.getBunchId()).orElseThrow { RuntimeException("bunch not found") }
                bunchRepository.delete(bunch)
            } else {
                // TODO master 없는 경우 양도
            }
        }

        memberRepository.delete(member)
    }

    fun getOneByEmail(email: String): Member? {
        return memberRepository.findByEmail(email).firstOrNull()
    }

    fun getBunchMembers(bunch: Bunch): Set<Member> {
        val bunchMembers = bunchMemberRepository.findByBunchId(bunch.id!!)
        return memberRepository.findByIdIn(bunchMembers.map { it.getMemberId() }.toSet())
    }
}