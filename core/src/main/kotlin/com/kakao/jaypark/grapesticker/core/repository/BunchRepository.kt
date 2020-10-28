package com.kakao.jaypark.grapesticker.core.repository

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import org.springframework.data.repository.CrudRepository

interface BunchRepository : CrudRepository<Bunch, String>{
}