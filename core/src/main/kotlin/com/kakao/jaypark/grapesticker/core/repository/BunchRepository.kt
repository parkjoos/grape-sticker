package com.kakao.jaypark.grapesticker.core.repository

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface BunchRepository : CrudRepository<Bunch, String> {
    fun findAllByIdIn(bunchIds: Set<String>): Set<Bunch>
}