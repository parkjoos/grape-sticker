package com.kakao.jaypark.grapesticker.core.repository

import com.kakao.jaypark.grapesticker.core.domain.BunchMember
import org.springframework.data.repository.CrudRepository

interface BunchMemberRepository : CrudRepository<BunchMember, String>{
}