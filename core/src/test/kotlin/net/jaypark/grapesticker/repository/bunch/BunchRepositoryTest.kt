package net.jaypark.grapesticker.repository.bunch

import net.jaypark.grapesticker.domain.Bunch
import net.jaypark.grapesticker.domain.Grape
import net.jaypark.grapesticker.repository.AbstractRepositoryTest
import net.jaypark.grapesticker.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class BunchRepositoryTest : AbstractRepositoryTest() {

    @Autowired
    lateinit var bunchRepository: BunchRepository

    @Test
    fun testSave() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        assertThat(bunch).extracting("id").isNotNull
        assertThat(bunch).extracting("lastModifiedDate").isNotNull
    }

    @Test
    fun testUpdateWithGrape() {
        val bunch = Bunch(name = "포도송이1", maxNumberOfGrapes = 10)
        bunchRepository.save(bunch)
        assertThat(bunch).extracting("id").isNotNull
        assertThat(bunch.createdDate).isNotNull
        assertThat(bunch.lastModifiedDate).isNotNull
        val createdDate = bunch.createdDate;
        val lastModifiedDate = bunch.lastModifiedDate;

        val grape = Grape(position = 5, comment = "first grape", writerId = "testWriterId")
        bunch.attachGrape(grape)
        bunchRepository.save(bunch)

        assertThat(bunch.createdDate).isEqualTo(createdDate)
        assertThat(bunch.lastModifiedDate).isNotNull
        assertThat(bunch.lastModifiedDate).isNotEqualTo(lastModifiedDate)

        val grape2 = Grape(position = 3, comment = "first grape", writerId = "testWriterId")
        bunch.attachGrape(grape2)
        bunchRepository.save(bunch)

        grape2.modify(grape2.copy(comment = "alter comment"))
        bunchRepository.save(bunch)
        assertThat(grape2.comment).isEqualTo("alter comment")

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