package com.kakao.jaypark.grapesticker.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

@DynamoDBDocument
data class Grape(
        @DynamoDBAttribute
        @NotNull
        var position: Int,

        @DynamoDBAttribute
        @NotNull
        var createdDate: LocalDateTime? = null,

        @DynamoDBAttribute
        @NotNull
        var lastModifiedDate: LocalDateTime? = null,

        @DynamoDBAttribute
        var comment: String? = null,

        @DynamoDBAttribute
        @NotNull
        var writerId: String? = null
) {
        public fun modify(grapeToModify: Grape) {
                if (this.position != grapeToModify.position) {
                        throw IllegalArgumentException("can modify only same position")
                }
                this.comment = grapeToModify.comment
                this.writerId = grapeToModify.writerId
                this.lastModifiedDate = LocalDateTime.now()
        }
}
