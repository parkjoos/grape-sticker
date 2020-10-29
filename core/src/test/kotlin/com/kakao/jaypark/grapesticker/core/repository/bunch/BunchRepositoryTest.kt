package com.kakao.jaypark.grapesticker.core.repository.bunch

import com.kakao.jaypark.grapesticker.core.CoreApplication
import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.repository.AbstractRepositoryTest
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

class BunchRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var bunchRepository: BunchRepository

    @Test
    fun test() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        assertThat(bunch).extracting("id").isNotNull()

    }
}