package com.kakao.jaypark.grapesticker.core.repository

import com.kakao.jaypark.grapesticker.core.domain.Member
import org.springframework.data.repository.CrudRepository

interface MemberRepository : CrudRepository<Member, String>{
}