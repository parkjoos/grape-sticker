package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.api.controller.to.GrapeTO
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

    @PutMapping("/{bunchId}")
    fun get(@PathVariable bunchId: String, @RequestBody bunchTO: BunchTO) {
        val bunch = bunchTO.buildBunch()
        bunch.id = bunchId
        bunchService.modify(bunch)
    }

    @GetMapping("/{bunchId}/grapes")
    fun getGrapes(@PathVariable bunchId: String): Set<GrapeTO> {
        val bunch = bunchService.get(bunchId)
        val writerIdSet = bunch.grapes
                ?.map { it.writerId }
                ?.toSet()
                .orEmpty()
        val map = memberService.getMap(writerIdSet)
        return bunch.grapes
                ?.map { GrapeTO.build(it, map.getValue(it.writerId)) }
                ?.toSet()
                .orEmpty()

    }
}