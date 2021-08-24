package net.jaypark.grapesticker.api.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HomeController::class)
class HomeControllerTest(@Autowired val mockMvc: MockMvc) {

    @Test
    fun aliveTest() {
        mockMvc.perform(get("/alive"))
                .andDo(print())
                .andExpect(status().isOk)
    }
}