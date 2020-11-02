package com.kakao.jaypark.grapesticker.repository


import com.kakao.jaypark.grapesticker.CoreApplication
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [CoreApplication::class])
@ActiveProfiles("test")
@TestPropertySource(properties = ["embedded-dynamodb.use=true"])
abstract class AbstractRepositoryTest() {
}