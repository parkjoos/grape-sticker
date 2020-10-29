package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.springframework.stereotype.Service

@Service
class BunchService (
        var bunchRepository: BunchRepository
){
    fun create(bunch: Bunch) {
        bunchRepository.save(bunch)
    }

    fun get(bunchId: String): Bunch {
        return bunchRepository.findById(bunchId).orElseThrow { RuntimeException("can not find bunch") }
    }

}
