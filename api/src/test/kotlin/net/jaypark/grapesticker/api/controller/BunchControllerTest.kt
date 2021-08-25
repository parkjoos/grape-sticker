package net.jaypark.grapesticker.api.controller

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.jaypark.grapesticker.api.controller.to.BunchTO
import net.jaypark.grapesticker.api.controller.to.GrapeTO
import net.jaypark.grapesticker.api.security.CustomOAuth2UserService
import net.jaypark.grapesticker.api.security.GrapeStickerOAuth2User
import net.jaypark.grapesticker.api.security.OAuthAttributes
import net.jaypark.grapesticker.domain.Bunch
import net.jaypark.grapesticker.domain.Grape
import net.jaypark.grapesticker.domain.Member
import net.jaypark.grapesticker.domain.enums.GrapeStickerType
import net.jaypark.grapesticker.domain.enums.MemberStatus
import net.jaypark.grapesticker.service.BunchService
import net.jaypark.grapesticker.service.GrapeStickerService
import net.jaypark.grapesticker.service.MemberService
import org.apache.commons.codec.CharEncoding
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime


@WebMvcTest(controllers = [BunchController::class])
@WithMockUser
class BunchControllerTest(@Autowired val mockMvc: MockMvc) {
    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
        Mockito.`when`(methodCall)

    @MockBean
    lateinit var bunchService: BunchService

    @MockBean
    lateinit var grapeStickerService: GrapeStickerService

    @MockBean
    lateinit var memberService: MemberService

    @MockBean
    lateinit var customOAuth2UserService: CustomOAuth2UserService

    @Test
    fun testCreate() {
        val bunch = BunchTO(name = "new grape bunch1", maxNumberOfGrapes = 15, stickerType = GrapeStickerType.PRAISE)
        mockMvc.perform(
            post("/bunches")
                .content(Json.encodeToString(bunch))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(CharEncoding.UTF_8)
                .with(csrf())
        )
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun getBunchWithMember() {
        val expectedBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
            .thenReturn(expectedBunch)
        whenever(memberService.getBunchMembers(expectedBunch))
            .thenReturn(
                hashSetOf(
                    Member(
                        id = "emember34r-134r234r-2345r234r",
                        email = "jay.park@kakao.com",
                        name = "jay.park",
                        status = MemberStatus.VALID
                    )
                )
            )
        mockMvc.perform(get("/bunches/3465y356y-3546y4567u47-3546g356g"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("createdDate").isNotEmpty)
            .andExpect(jsonPath("members").isArray)
    }

    @Test
    fun getGrapes() {
        val expectedBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            grapes = hashSetOf(
                Grape(position = 1, comment = "test", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 2, comment = "test2", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 5, comment = "test3", writerId = "eme234rf34r-q34fq34-vse4rf3e")
            ),
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
            .thenReturn(expectedBunch)

        whenever(memberService.getMap(hashSetOf("emember34r-134r234r-2345r234r", "eme234rf34r-q34fq34-vse4rf3e")))
            .thenReturn(
                hashMapOf(
                    Pair(
                        "emember34r-134r234r-2345r234r",
                        Member(
                            id = "emember34r-134r234r-2345r234r",
                            email = "jay.park@kakao.com",
                            name = "jay.park",
                            status = MemberStatus.VALID
                        )
                    ),
                    Pair(
                        "eme234rf34r-q34fq34-vse4rf3e",
                        Member(
                            id = "eme234rf34r-q34fq34-vse4rf3e",
                            email = "jay.park@kakao.com",
                            name = "jay.park2",
                            status = MemberStatus.VALID
                        )
                    )
                )
            )

        mockMvc.perform(get("/bunches/3465y356y-3546y4567u47-3546g356g/grapes"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("[0]").isNotEmpty)
            .andExpect(jsonPath("[2]").isNotEmpty)
            .andDo(print())
    }

    @Test
    fun testGetBunchesAllOfMine() {
        val expectedBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        val loginMember = Member(id = "2345-234523-35435-345g345", oAuth2Id = "436452433")
        val grapeStickerOAuth2User = GrapeStickerOAuth2User(
            member = loginMember,
            oAuthAttributes = OAuthAttributes(
                name = "username",
                attributes = mutableMapOf(),
                registrationId = "kakao",
                userNameAttributeName = "id",
                id = loginMember.oAuth2Id,
                email = null
            ),
            authorities = mutableSetOf(GrantedAuthority { "ROLE_USER" })
        )
        whenever(memberService.get(loginMember.id!!)).thenReturn(loginMember)
        whenever(bunchService.getAllBunchesByMember(loginMember)).thenReturn(
            hashSetOf(
                expectedBunch,
                expectedBunch.copy(id = "34tr32t-se57er675-5436y3456y", name = "bunch name sample2"),
                expectedBunch.copy(id = "235tasetgf-sae5y354tg-0aw4634", name = "bunch name sample3")
            )
        )
        mockMvc.perform(
            get("/bunches")
                .with(oauth2Login().oauth2User(grapeStickerOAuth2User))
        ).andExpect(status().isOk)
            .andExpect(jsonPath("[0]").isNotEmpty)
            .andDo(print())
    }

    @Test
    fun testModifyBunch() {
        val beforeBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            grapes = hashSetOf(
                Grape(position = 1, comment = "test", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 2, comment = "test2", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 5, comment = "test3", writerId = "eme234rf34r-q34fq34-vse4rf3e")
            ),
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("2352345-245t6356-qerg24356"))
            .thenReturn(beforeBunch);

        val bunch = BunchTO(name = "modify grape bunch1", maxNumberOfGrapes = 20, stickerType = GrapeStickerType.PRAISE)
        mockMvc.perform(
            put("/bunches/2352345-245t6356-qerg24356")
                .content(Json.encodeToString(bunch))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(CharEncoding.UTF_8)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andDo(print())
    }

    @Test
    fun testDeleteBunch() {
        val expectedBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
            .thenReturn(expectedBunch)
        mockMvc.perform(delete("/bunches/3465y356y-3546y4567u47-3546g356g").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun testAttachGrape() {
        val loginMember = Member(id = "2345-234523-35435-345g345", oAuth2Id = "436452433")
        val grapeStickerOAuth2User = GrapeStickerOAuth2User(
            member = loginMember,
            oAuthAttributes = OAuthAttributes(
                name = "username",
                attributes = mutableMapOf(),
                registrationId = "kakao",
                userNameAttributeName = "id",
                id = loginMember.oAuth2Id,
                email = null
            ),
            authorities = mutableSetOf(GrantedAuthority { "ROLE_USER" })
        )
        val grape = GrapeTO(position = 1, comment = "test")

        mockMvc.perform(
            post("/bunches/3465y356y-3546y4567u47-3546g356g/grapes")
                .content(Json.encodeToString(grape))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(CharEncoding.UTF_8)
                .with(csrf())
                .with(oauth2Login().oauth2User(grapeStickerOAuth2User))
        )
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun testRemoveGrape() {
        val expectedBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            grapes = hashSetOf(
                Grape(position = 1, comment = "test", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 2, comment = "test2", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 5, comment = "test3", writerId = "eme234rf34r-q34fq34-vse4rf3e")
            ),
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
            .thenReturn(expectedBunch)
        mockMvc.perform(
            delete("/bunches/3465y356y-3546y4567u47-3546g356g/grapes/position/1")
                .with(csrf())
        )
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun testModifyGrape() {
        val beforeBunch = Bunch(
            id = "3465y356y-3546y4567u47-3546g356g",
            name = "bunch name sample1",
            maxNumberOfGrapes = 15,
            stickerType = GrapeStickerType.PRAISE,
            grapes = hashSetOf(
                Grape(position = 1, comment = "test", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 2, comment = "test2", writerId = "emember34r-134r234r-2345r234r"),
                Grape(position = 5, comment = "test3", writerId = "eme234rf34r-q34fq34-vse4rf3e")
            ),
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now()
        )
        whenever(bunchService.get("2352345-245t6356-qerg24356"))
            .thenReturn(beforeBunch);

        val grape = GrapeTO(position = 1, comment = "test modify")
        mockMvc.perform(
            put("/bunches/3465y356y-3546y4567u47-3546g356g/grapes")
                .content(Json.encodeToString(grape))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(CharEncoding.UTF_8)
                .with(csrf())
        )
            .andExpect(status().isOk)
            .andDo(print())
    }
}