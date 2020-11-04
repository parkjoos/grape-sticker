package com.kakao.jaypark.grapesticker.api.controller

import com.kakao.jaypark.grapesticker.api.controller.to.BunchTO
import com.kakao.jaypark.grapesticker.domain.Bunch
import com.kakao.jaypark.grapesticker.domain.Grape
import com.kakao.jaypark.grapesticker.domain.Member
import com.kakao.jaypark.grapesticker.domain.enums.GrapeStickerType
import com.kakao.jaypark.grapesticker.domain.enums.MemberStatus
import com.kakao.jaypark.grapesticker.service.BunchService
import com.kakao.jaypark.grapesticker.service.MemberService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.commons.codec.CharEncoding
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(BunchController::class)
class BunchControllerTest(@Autowired val mockMvc: MockMvc) {
    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @MockBean
    lateinit var bunchService: BunchService

    @MockBean
    lateinit var memberService: MemberService

    @Test
    fun testCreate() {
        val bunch = BunchTO(name = "new grape bunch1", maxNumberOfGrapes = 15, stickerType = GrapeStickerType.PRAISE)
        mockMvc.perform(post("/bunches")
                .content(Json.encodeToString(bunch))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(CharEncoding.UTF_8))
                .andDo(print())
                .andExpect(status().isOk)
    }

    @Test
    fun getBunchWithMember() {
        val expectedBunch = Bunch(id = "3465y356y-3546y4567u47-3546g356g",
                name = "bunch name sample1",
                maxNumberOfGrapes = 15,
                stickerType = GrapeStickerType.PRAISE,
                createdDate = LocalDateTime.now(),
                lastModifiedDate = LocalDateTime.now())
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
                .thenReturn(expectedBunch)
        whenever(memberService.getBunchMembers(expectedBunch))
                .thenReturn(hashSetOf(Member(id = "emember34r-134r234r-2345r234r", email = "jay.park@kakao.com", name = "jay.park", status = MemberStatus.VALID)))
        mockMvc.perform(get("/bunches/3465y356y-3546y4567u47-3546g356g"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(jsonPath("createdDate").isNotEmpty)
                .andExpect(jsonPath("members").isArray)
    }

    @Test
    fun getGrapes() {
        val expectedBunch = Bunch(id = "3465y356y-3546y4567u47-3546g356g",
                name = "bunch name sample1",
                maxNumberOfGrapes = 15,
                stickerType = GrapeStickerType.PRAISE,
                grapes = hashSetOf(Grape(position = 1, comment = "test", writerId = "emember34r-134r234r-2345r234r"),
                        Grape(position = 2, comment = "test2", writerId = "emember34r-134r234r-2345r234r"),
                        Grape(position = 5, comment = "test3", writerId = "eme234rf34r-q34fq34-vse4rf3e")),
                createdDate = LocalDateTime.now(),
                lastModifiedDate = LocalDateTime.now())
        whenever(bunchService.get("3465y356y-3546y4567u47-3546g356g"))
                .thenReturn(expectedBunch)

        whenever(memberService.getMap(hashSetOf("emember34r-134r234r-2345r234r", "eme234rf34r-q34fq34-vse4rf3e")))
                .thenReturn(hashMapOf(Pair("emember34r-134r234r-2345r234r", Member(id = "emember34r-134r234r-2345r234r", email = "jay.park@kakao.com", name = "jay.park", status = MemberStatus.VALID)),
                        Pair("eme234rf34r-q34fq34-vse4rf3e", Member(id = "eme234rf34r-q34fq34-vse4rf3e", email = "jay.park@kakao.com", name = "jay.park2", status = MemberStatus.VALID))))

        mockMvc.perform(get("/bunches/3465y356y-3546y4567u47-3546g356g/grapes"))
                .andExpect(status().isOk)
                .andDo(print())
    }
}