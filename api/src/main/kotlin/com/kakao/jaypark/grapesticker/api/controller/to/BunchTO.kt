package com.kakao.jaypark.grapesticker.api.controller.to

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.GrapeStickerType
import java.time.LocalDateTime

data class BunchTO(
        var name: String,
        var maxNumberOfGrapes: Int,
        var stickerType: GrapeStickerType,
        var members: Set<Member>? = null,
        var createdDate: LocalDateTime? = null,
        var lastModifiedDate: LocalDateTime? = null
) {
    fun buildBunch(): Bunch {
        return Bunch(name = name, maxNumberOfGrapes = maxNumberOfGrapes, stickerType = stickerType)
    }

    fun build(bunch: Bunch, members: Set<Member>): BunchTO {
        return BunchTO(name = bunch.name!!,
                maxNumberOfGrapes = bunch.maxNumberOfGrapes,
                stickerType = bunch.stickerType,
                createdDate = bunch.createdDate!!,
                lastModifiedDate = bunch.lastModifiedDate!!,
                members = members)
    }

}
