package com.kakao.jaypark.grapesticker.core.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.kakao.jaypark.grapesticker.core.domain.converter.LocalDateTimeConverter
import java.time.LocalDateTime

@DynamoDBDocument
data class Grape(
        @DynamoDBAttribute
        var position: Int,
        @DynamoDBAttribute
        @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
        var createdDate: LocalDateTime? = null,
        @DynamoDBAttribute
        var comment: String? =  null,
        @DynamoDBAttribute
        var writerId: String
)
