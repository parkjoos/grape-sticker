package com.kakao.jaypark.grapesticker.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted
import com.kakao.jaypark.grapesticker.domain.converter.LocalDateTimeConverter
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

@DynamoDBDocument
data class Grape(
        @DynamoDBAttribute
        @NotNull
        var position: Int,

        @DynamoDBAttribute
        @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
        @NotNull
        var createdDate: LocalDateTime? = null,

        @DynamoDBAttribute
        var comment: String? = null,

        @DynamoDBAttribute
        @NotNull
        var writerId: String
)
