package com.kakao.jaypark.grapesticker.core.domain

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import org.springframework.data.annotation.Id

@DynamoDBTable(tableName = "bunch-member")
data class BunchMember(
        @Id
        @DynamoDBIgnore
        val key: BunchMemberKey
){
        @DynamoDBHashKey
        fun getBunchId() = key.bunchId

        fun setBunchId(bunchId: String) {
                key.bunchId = bunchId
        }

        @DynamoDBRangeKey
        fun getMemberId() = key.memberId

        fun setMemberId(memberId: String) {
                key.memberId = memberId
        }
}