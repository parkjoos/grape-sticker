package com.kakao.jaypark.grapesticker.core.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument

@DynamoDBDocument
data class Grape(
        @DynamoDBAttribute
        var position: Int,
//        @DynamoDBAttribute
//        var date: LocalDateTime,
        @DynamoDBAttribute
        var type: GrapeType = GrapeType.PRAISE,
        @DynamoDBAttribute
        var comment: String? =  null,
        @DynamoDBAttribute
        var writerId: String
) {

}
