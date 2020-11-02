package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.service.BunchService
import com.kakao.jaypark.grapesticker.service.MemberService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/bunches")
class BunchController(
        private var bunchService: BunchService,
        private var memberService: MemberService
) {
    @PostMapping
    fun create(@RequestBody bunchTO: BunchTO) {
        bunchService.create(bunchTO.buildBunch(), Member())
    }
}