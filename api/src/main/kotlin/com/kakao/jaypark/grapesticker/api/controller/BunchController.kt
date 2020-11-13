package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.api.controller.to.GrapeTO
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.service.BunchService
import com.kakao.jaypark.grapesticker.service.GrapeStickerService
import com.kakao.jaypark.grapesticker.service.MemberService
import org.springframework.web.bind.annotation.*
@RestController
@RequestMapping("/bunches")
class BunchController(
        private val bunchService: BunchService,
        private val grapeStickerService: GrapeStickerService,
        private val memberService: MemberService
) {

    companion object {
        val LOGIN_MEMBER = Member("test-member-id", "testMember@kakao.com", "testName", MemberStatus.VALID)
    }

    @PostMapping
    fun create(@RequestBody bunchTO: BunchTO) {
        bunchService.create(bunchTO.buildBunch(), Member())

    }

    @GetMapping
    fun getAllOfMine(): List<BunchTO> {
        val bunches = bunchService.getAllBunchesByMember(LOGIN_MEMBER)
        return bunches.map { BunchTO.build(it) }
    }

    @GetMapping("/{bunchId}")
    fun get(@PathVariable bunchId: String): BunchTO {
        val bunch = bunchService.get(bunchId)
        val members = memberService.getBunchMembers(bunch)
        return BunchTO.build(bunch, members)
    }

    @PutMapping("/{bunchId}")
    fun modify(@PathVariable bunchId: String, @RequestBody bunchTO: BunchTO) {
        val bunch = bunchTO.buildBunch()
        bunch.id = bunchId
        bunchService.modify(bunch)
    }

    @DeleteMapping("/{bunchId}")
    fun delete(@PathVariable bunchId: String) {
        val bunch = bunchService.get(bunchId)
        bunchService.delete(bunch)
    }

    @GetMapping("/{bunchId}/grapes")
    fun getGrapes(@PathVariable bunchId: String): Set<GrapeTO> {
        val bunch = bunchService.get(bunchId)
        val writerIdSet = bunch.grapes
                ?.map { it.writerId!! }
                ?.toSet()
                .orEmpty()
        val map = memberService.getMap(writerIdSet)
        return bunch.grapes
                ?.map { GrapeTO.build(it, map.getValue(it.writerId!!)) }
                ?.toSet()
                .orEmpty()

    }

    @PostMapping("/{bunchId}/grapes")
    fun attachGrapes(@PathVariable bunchId: String, @RequestBody grapeTO: GrapeTO) {
        val bunch = bunchService.get(bunchId)
        grapeStickerService.attach(bunch, grapeTO.buildGrape(), LOGIN_MEMBER)

    }

    @PutMapping("/{bunchId}/grapes")
    fun modifyGrapes(@PathVariable bunchId: String, @RequestBody grapeTO: GrapeTO) {
        val bunch = bunchService.get(bunchId)
        grapeStickerService.modify(bunch, grapeTO.buildGrape())
    }

    @DeleteMapping("/{bunchId}/grapes/position/{position}")
    fun removeGrapes(@PathVariable bunchId: String, @PathVariable position: Int) {
        val bunch = bunchService.get(bunchId)
        grapeStickerService.remove(bunch, position)

    }
}

