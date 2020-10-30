package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.Grape
import com.kakao.jaypark.grapesticker.core.domain.Member
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class GrapeStickerService (
        var bunchRepository: BunchRepository
){
    fun attach(bunch: Bunch, grape: Grape, member: Member) {
        grape.createdDate = LocalDateTime.now()
        grape.writerId = member.id!!
        bunch.attachGrape(grape)
        bunchRepository.save(bunch)
    }
}