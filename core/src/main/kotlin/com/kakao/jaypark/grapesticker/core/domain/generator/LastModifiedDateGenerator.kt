package com.kakao.jaypark.grapesticker.core.domain.generator

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerateStrategy
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGenerator
import java.time.LocalDateTime

class LastModifiedDateGenerator : DynamoDBAutoGenerator<LocalDateTime> {
    override fun generate(currentValue: LocalDateTime?): LocalDateTime {
        return LocalDateTime.now()
    }

    override fun getGenerateStrategy(): DynamoDBAutoGenerateStrategy {
        return DynamoDBAutoGenerateStrategy.ALWAYS
    }
}