package com.kakao.jaypark.grapesticker.core.repository.bunch

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.domain.Grape
import com.kakao.jaypark.grapesticker.core.repository.AbstractRepositoryTest
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime

class BunchRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var bunchRepository: BunchRepository

    @Test
    fun testSave() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        assertThat(bunch).extracting("id").isNotNull()
    }

    @Test
    fun testUpdateWithGrape() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        assertThat(bunch).extracting("id").isNotNull()

        val grape = Grape(position = 5, comment = "first grape", writerId = "testWriterId", createdDate = LocalDateTime.now())
        bunch.attachGrape(grape)
        bunchRepository.save(bunch)

        val grape2 = Grape(position = 3, comment = "first grape", writerId = "testWriterId", createdDate = LocalDateTime.now())
        bunch.attachGrape(grape2)
        bunchRepository.save(bunch)
    }

    @Test
    fun testFindIn() {
        val bunch1 = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch1)

        val bunch2 = Bunch(name = "포도송이2", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch2)

        val bunch3 = Bunch(name = "포도송이3", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch3)

        val bunch4 = Bunch(name = "포도송이4", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch4)

        val result = bunchRepository.findAllByIdIn(HashSet(listOf(bunch1.id.orEmpty(), bunch2.id.orEmpty(), bunch4.id.orEmpty())))

        assertThat(result).allSatisfy {
            assertThat(it.id).isNotEqualTo(bunch3.id)
        }
    }
}