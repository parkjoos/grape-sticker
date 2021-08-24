package net.jaypark.grapesticker.api.controller.to

import kotlinx.serialization.Serializable
import net.jaypark.grapesticker.domain.Member
import net.jaypark.grapesticker.domain.enums.MemberStatus

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
