package net.jaypark.grapesticker.api.controller

import net.jaypark.grapesticker.api.controller.to.BunchTO
import net.jaypark.grapesticker.api.controller.to.GrapeTO
import net.jaypark.grapesticker.api.security.GrapeStickerOAuth2User
import net.jaypark.grapesticker.domain.Member
import net.jaypark.grapesticker.service.BunchService
import net.jaypark.grapesticker.service.GrapeStickerService
import net.jaypark.grapesticker.service.MemberService
import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/bunches")
class BunchController(
        private val bunchService: BunchService,
        private val grapeStickerService: GrapeStickerService,
        private val memberService: MemberService
) {

    @PostMapping
    fun create(@RequestBody bunchTO: BunchTO) {
        bunchService.create(bunchTO.buildBunch(), Member())

    }

    @GetMapping
    fun getAllOfMine(@AuthenticationPrincipal authenticatedPrincipal: AuthenticatedPrincipal): List<BunchTO> {
        val memberId = (authenticatedPrincipal as GrapeStickerOAuth2User).getMemberId()
        val loginMember = memberService.get(memberId)
        val bunches = bunchService.getAllBunchesByMember(loginMember)
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
    fun attachGrapes(
        @PathVariable bunchId: String,
        @RequestBody grapeTO: GrapeTO,
        @AuthenticationPrincipal authenticatedPrincipal: AuthenticatedPrincipal
    ) {
        val memberId = (authenticatedPrincipal as GrapeStickerOAuth2User).getMemberId()
        val loginMember = memberService.get(memberId)
        val bunch = bunchService.get(bunchId)
        grapeStickerService.attach(bunch, grapeTO.buildGrape(), loginMember)

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

