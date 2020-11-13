package com.kakao.jaypark.grapesticker.api.controller.to

import com.kakao.jaypark.grapesticker.api.controller.serializer.LocalDateTimeSerializer
import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.GrapeStickerType
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class BunchTO(
        var name: String,
        var maxNumberOfGrapes: Int,
        var stickerType: GrapeStickerType,
        var members: Set<MemberTO>? = null,
        @Serializable(with = LocalDateTimeSerializer::class)
        var createdDate: LocalDateTime? = null,
        @Serializable(with = LocalDateTimeSerializer::class)
        var lastModifiedDate: LocalDateTime? = null
) {
    fun buildBunch(): Bunch {
        return Bunch(name = name, maxNumberOfGrapes = maxNumberOfGrapes, stickerType = stickerType)
    }

    companion object {
        fun build(bunch: Bunch, members: Set<Member>): BunchTO {
            return BunchTO(name = bunch.name!!,
                    maxNumberOfGrapes = bunch.maxNumberOfGrapes,
                    stickerType = bunch.stickerType,
                    createdDate = bunch.createdDate!!,
                    lastModifiedDate = bunch.lastModifiedDate!!,
                    members = members.map { MemberTO.build(it) }.toSet())
        }

        fun build(bunch: Bunch): BunchTO {
            return BunchTO(name = bunch.name!!,
                    maxNumberOfGrapes = bunch.maxNumberOfGrapes,
                    stickerType = bunch.stickerType,
                    createdDate = bunch.createdDate!!,
                    lastModifiedDate = bunch.lastModifiedDate!!)
        }
    }


}
