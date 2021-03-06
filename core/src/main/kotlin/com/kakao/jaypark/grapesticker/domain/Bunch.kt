package com.kakao.jaypark.grapesticker.domain

import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.kakao.jaypark.grapesticker.domain.converter.LocalDateTimeConverter
import com.kakao.jaypark.grapesticker.domain.enums.GrapeStickerType
import com.kakao.jaypark.grapesticker.domain.generator.CreatedDateGenerator
import com.kakao.jaypark.grapesticker.domain.generator.LastModifiedDateGenerator
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

@DynamoDBTable(tableName = "bunch")
data class Bunch(

        @NotNull
        @DynamoDBHashKey(attributeName = "id")
        @DynamoDBAutoGeneratedKey
        var id: String? = null,

        @NotNull
        @DynamoDBAttribute
        var name: String? = null,

        @NotNull
        @DynamoDBAttribute
        var maxNumberOfGrapes: Int = 10,

        @DynamoDBAttribute
        var grapes: HashSet<Grape>? = null,

        @NotNull
        @DynamoDBAttribute
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
        var stickerType: GrapeStickerType = GrapeStickerType.PRAISE,

        @NotNull
        @DynamoDBAttribute
        @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
        @DynamoDBAutoGenerated(generator = CreatedDateGenerator::class)
        var createdDate: LocalDateTime? = null,

        @NotNull
        @DynamoDBAttribute
        @DynamoDBTypeConverted(converter = LocalDateTimeConverter::class)
        @DynamoDBAutoGenerated(generator = LastModifiedDateGenerator::class)
        var lastModifiedDate: LocalDateTime? = null
) {
    fun attachGrape(grape: Grape) {
        if (grapes == null) {
            grapes = hashSetOf(grape)
        } else {
            if (grapes?.any {
                        it.position == grape.position
                    }!!) {
                throw RuntimeException("grape position duplicated")
            }
            grapes?.add(grape)
        }
    }

    fun modify(bunchToModify: Bunch) {
        this.name = bunchToModify.name
        this.maxNumberOfGrapes = bunchToModify.maxNumberOfGrapes
    }
}