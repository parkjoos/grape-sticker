package net.jaypark.grapesticker.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(properties = ["embedded-dynamodb.use=true"])
class ApiApplicationTests {

    @Test
    fun contextLoads() {
    }

}
