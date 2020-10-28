package com.kakao.jaypark.grapesticker.core.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMappingException
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary


@Configuration
@EnableDynamoDBRepositories(basePackages = ["com.kakao.jaypark.grapesticker.core.repository"])
class DynamoDBRepositoryConfiguration {
    @Value("\${amazon.dynamodb.endpoint}")
    private val amazonDynamoDbEndpoint: String? = null
    @Value("\${amazon.dynamodb.region}")
    private val amazonDynamoDbRegion: String? = null
    @Value("\${amazon.aws.accesskey}")
    private val amazonAwsAccessKey: String? = null
    @Value("\${amazon.aws.secretkey}")
    private val amazonAwsSecretKey: String? = null
    @Value("\${amazon.dynamodb.tablePrefix}")
    private val tablePrefix: String? = null
    @Value("\${amazon.dynamodb.tableSuffix}")
    private val tableSuffix: String? = null

    @Bean
    @Primary
    fun dynamoDBMapperConfig(): DynamoDBMapperConfig? {
        return DynamoDBMapperConfig.builder()
                .withTableNameResolver { clazz, config ->
                    val dynamoDBTable = clazz.getDeclaredAnnotation(DynamoDBTable::class.java)
                            ?: throw DynamoDBMappingException("$clazz not annotated with @DynamoDBTable")
                    return@withTableNameResolver tablePrefix + "-" + dynamoDBTable.tableName + "-" +tableSuffix
                }.build()
    }

    @Bean
    @Primary
    fun dynamoDBMapper(amazonDynamoDb: AmazonDynamoDB?, config: DynamoDBMapperConfig?): DynamoDBMapper {
        return DynamoDBMapper(amazonDynamoDb, config)
    }

    @Bean(name = ["amazonDynamoDB"])
    fun amazonDynamoDb(): AmazonDynamoDB {
        val credentialsProvider = AWSStaticCredentialsProvider(
                BasicAWSCredentials(amazonAwsAccessKey, amazonAwsSecretKey))
        val endpointConfiguration = EndpointConfiguration(amazonDynamoDbEndpoint, amazonDynamoDbRegion)
        return AmazonDynamoDBClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(endpointConfiguration)
                .build()
    }
}