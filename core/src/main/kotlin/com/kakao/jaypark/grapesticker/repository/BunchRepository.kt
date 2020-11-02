package com.kakao.jaypark.grapesticker.repository

import com.kakao.jaypark.grapesticker.domain.Bunch
import org.socialsignin.spring.data.dynamodb.repository.EnableScan
import org.springframework.data.repository.CrudRepository

@EnableScan
interface BunchRepository : CrudRepository<Bunch, String> {
    fun findAllByIdIn(bunchIds: Set<String>): Set<Bunch>
}