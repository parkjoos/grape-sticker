package com.kakao.jaypark.grapesticker.core.service

import com.kakao.jaypark.grapesticker.core.domain.Bunch
import com.kakao.jaypark.grapesticker.core.repository.BunchRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.stubbing.OngoingStubbing
import java.util.*

@ExtendWith(MockitoExtension::class)
class BunchServiceTest {

    // To avoid having to use backticks for "when"
    fun <T> whenever(methodCall: T): OngoingStubbing<T> =
            Mockito.`when`(methodCall)

    @Mock
    private lateinit var bunchRepository: BunchRepository

    @InjectMocks
    private lateinit var bunchService: BunchService

    @Test
    fun testCreateBunch(){
        var bunch = Bunch(name = "테스트용 포토송이")
        bunchService.create(bunch);
    }

    @Test
    fun testGetBunch(){
        var testBunchId = "3w456tqew65-3456wsergt-23456";

        var expected = Bunch(id = testBunchId, name = "test bunch")
        whenever(bunchRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(expected))

        var result = bunchService.get(testBunchId)
        assertThat(result).extracting("id").isEqualTo(testBunchId);
    }

}