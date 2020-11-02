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
        grape.createdDate = LocalDateTime.now()
        grape.writerId = member.id!!
        bunch.attachGrape(grape)
        bunchRepository.save(bunch)
    }

    fun remove(bunch: Bunch, grape: Grape) {
        bunch.grapes?.removeIf { it.position == grape.position }
        bunchRepository.save(bunch)
    }

}