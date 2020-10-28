package com.kakao.jaypark.grapesticker.core.domain

import com.amazonaws.services.dynamodbv2.datamodeling.*
import java.io.Serializable

@DynamoDBDocument
data class BunchMemberKey(
        @DynamoDBHashKey(attributeName = "bunchId")
        @DynamoDBIndexRangeKey(globalSecondaryIndexName = "ByMember")
        var bunchId: String,

        @DynamoDBRangeKey(attributeName = "memberId")
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "ByMember")
        var memberId: String
) : Serializable
