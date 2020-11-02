package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.service.BunchService
import com.kakao.jaypark.grapesticker.service.MemberService
import org.springframework.web.bind.annotation.*

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

    @GetMapping("/{bunchId}")
    fun get(@PathVariable bunchId: String): BunchTO {
        val bunch = bunchService.get(bunchId)
        val members = memberService.getBunchMembers(bunch)
        return BunchTO.build(bunch, members)
    }
}