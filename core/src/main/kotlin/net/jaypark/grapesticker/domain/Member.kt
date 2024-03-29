package net.jaypark.grapesticker.domain

import com.amazonaws.services.dynamodbv2.datamodeling.*
import net.jaypark.grapesticker.domain.enums.MemberStatus
import javax.validation.constraints.NotNull

@DynamoDBTable(tableName = "member")
data class Member(
        @DynamoDBHashKey(attributeName = "id")
        @DynamoDBAutoGeneratedKey
        @NotNull
        var id: String? = null,

        @DynamoDBAttribute
        @DynamoDBIndexHashKey(globalSecondaryIndexName = "idx-member-email", attributeName = "email")
        @NotNull
        var email: String? = null,

        @DynamoDBAttribute
        @NotNull
        var name: String? = null,

        @DynamoDBAttribute
        @NotNull
        @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
        var status: MemberStatus = MemberStatus.VALID
)
