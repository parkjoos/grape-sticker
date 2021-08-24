package net.jaypark.grapesticker.api.controller.to

import kotlinx.serialization.Serializable
import net.jaypark.grapesticker.api.controller.serializer.LocalDateTimeSerializer
import net.jaypark.grapesticker.domain.Grape
import net.jaypark.grapesticker.domain.Member
import java.time.LocalDateTime

@Serializable
data class GrapeTO(
        var position: Int,
        @Serializable(with = LocalDateTimeSerializer::class)
        var createdDate: LocalDateTime? = null,
        var comment: String? = null,
        var writer: MemberTO? = null
) {
    fun buildGrape(): Grape {
        return Grape(position = position, comment = comment)
    }

    companion object {
        fun build(grape: Grape, member: Member): GrapeTO {
            return GrapeTO(grape.position, grape.createdDate, grape.comment!!, MemberTO.build(member))
        }
    }
}
