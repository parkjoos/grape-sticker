package com.kakao.jaypark.grapesticker.service

import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.Grape
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.repository.BunchRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GrapeStickerService(
        var bunchRepository: BunchRepository
) {
    fun attach(bunch: Bunch, grape: Grape, member: Member) {

        if (bunch.maxNumberOfGrapes <= bunch.grapes?.size ?: 0) {
            throw RuntimeException("max number of grapes exceeded")
        }
        grape.writerId = member.id!!
        grape.createdDate = LocalDateTime.now()
        grape.lastModifiedDate = LocalDateTime.now()
        bunch.attachGrape(grape)
        bunchRepository.save(bunch)
    }

    fun remove(bunch: Bunch, grape: Grape) {
        bunch.grapes?.removeIf { it.position == grape.position }
        bunchRepository.save(bunch)
    }

    fun modify(bunch: Bunch, grapeToModify: Grape) {
        val grape = (bunch.grapes?.find { it.position == grapeToModify.position }
                ?: throw RuntimeException("no grape at position"))
        grape.modify(grapeToModify)
        bunchRepository.save(bunch)
    }

}