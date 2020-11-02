package com.kakao.jaypark.grapesticker.api.controller.to

import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.MemberStatus
import kotlinx.serialization.Serializable

@Serializable
data class MemberTO(
        var email: String,
        var name: String,
        var status: MemberStatus? = null
) {
    companion object {
        fun build(member: Member): MemberTO {
            return MemberTO(email = member.email!!, name = member.name!!, status = member.status)
        }
    }
}
