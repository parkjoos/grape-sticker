package com.kakao.jaypark.grapesticker.service

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.BunchMember
import com.kakao.jaypark.grapesticker.domain.BunchMemberKey
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.domain.enums.MemberType
import com.kakao.jaypark.grapesticker.repository.BunchMemberRepository
import com.kakao.jaypark.grapesticker.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(
        private var memberRepository: MemberRepository,
        private var bunchMemberRepository: BunchMemberRepository,
        private var bunchService: BunchService
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
        get(member.id!!)

        val bunchesByMember = bunchMemberRepository.findAllByMemberId(member.id!!)
        bunchesByMember.forEach {
            bunchMemberRepository.delete(it)
            val bunch = bunchService.get(it.getBunchId())
            val bunchMembers = bunchMemberRepository.findByBunchId(bunch.id!!)
            if (bunchMembers.isEmpty()) {
                bunchService.delete(bunch)
            } else {
                electMaster(bunch)
            }
        }

        memberRepository.delete(member)
    }

    fun get(memberId: String): Member {
        return memberRepository.findById(memberId)
                .orElseThrow { RuntimeException("member not exists") }
    }

    fun getOneByEmail(email: String): Member? {
        return memberRepository.findByEmail(email).firstOrNull()
    }

    fun getBunchMembers(bunch: Bunch): Set<Member> {
        val bunchMembers = bunchMemberRepository.findByBunchId(bunch.id!!)
        return memberRepository.findByIdIn(bunchMembers.map { it.getMemberId() }.toSet())
    }

    fun modifyName(memberId: String, newName: String) {
        val member = get(memberId)
        member.name = newName
        memberRepository.save(member)

    }

    fun electMaster(bunch: Bunch) {
        // 이미 master 가 있으면 독재 없으면 제일 오래된 사람이 master
        val bunchMembers = bunchMemberRepository.findByBunchId(bunch.id!!)
        if (bunchMembers.stream().anyMatch { it.type == MemberType.MASTER }) {
            return
        }
        val oldestMember = bunchMembers.minByOrNull { member -> member.createdDate!! }
        makeMaster(bunch, get(oldestMember?.getMemberId()!!))
    }

    fun makeMaster(bunch: Bunch, member: Member) {
        val bunchMember = bunchMemberRepository.findByBunchIdAndMemberId(bunch.id!!, member.id!!)
                ?: throw RuntimeException("not a bunch member")
        if (bunchMember.type == MemberType.MASTER) {
            throw RuntimeException("already Master")
        }
        bunchMember.type = MemberType.MASTER
        bunchMemberRepository.save(bunchMember)
    }

    fun addToBunch(bunch: Bunch, member: Member) {
        get(member.id!!)
        val findByBunchIdAndMemberId = bunchMemberRepository.findByBunchIdAndMemberId(bunch.id!!, member.id!!)
        if (findByBunchIdAndMemberId != null) {
            throw RuntimeException("already member")
        }
        bunchMemberRepository.save(BunchMember(BunchMemberKey(bunchId = bunch.id!!, memberId = member.id!!), type = MemberType.MEMBER))
    }

    fun removeFromBunch(bunch: Bunch, member: Member) {
        get(member.id!!)
        val bunchMember = bunchMemberRepository.findByBunchIdAndMemberId(bunch.id!!, member.id!!)
                ?: throw RuntimeException("already removed")
        bunchMemberRepository.delete(bunchMember)
    }

    fun getMap(idSet: Set<String>): Map<String, Member> {
        val members = memberRepository.findByIdIn(idSet)
        return members.map { it.id!! to it }.toMap()
    }
}