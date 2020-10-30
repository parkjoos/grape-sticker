package com.kakao.jaypark.grapesticker.core.domain

import com.amazonaws.services.dynamodbv2.datamodeling.*
import java.io.Serializable

@DynamoDBDocument
data class BunchMemberKey(
        @DynamoDBHashKey(attributeName = "bunchId")
        @DynamoDBIndexRangeKey(globalSecondaryIndexName = "idx-by-memberId")
        var bunchId: String? = null,

        @DynamoDBRangeKey(attributeName = "memberId")
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "idx-by-memberId")
        var memberId: String? = null
) : Serializable
