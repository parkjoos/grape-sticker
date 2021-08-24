package net.jaypark.grapesticker.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder
import com.amazonaws.services.dynamodbv2.datamodeling.*
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.*
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile


@Configuration
@Profile(*["test", "dev", "production"])
@EnableDynamoDBRepositories(
    basePackages = ["net.jaypark.grapesticker.repository"],
    dynamoDBMapperConfigRef = "dynamoDBMapperConfig"
)
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
        return builder()
                .withSaveBehavior(SaveBehavior.UPDATE)
                .withConsistentReads(ConsistentReads.EVENTUAL)
                .withPaginationLoadingStrategy(PaginationLoadingStrategy.LAZY_LOADING)
                .withBatchWriteRetryStrategy(DefaultBatchWriteRetryStrategy.INSTANCE)
                .withBatchLoadRetryStrategy(DefaultBatchLoadRetryStrategy.INSTANCE)
                .withTypeConverterFactory(DynamoDBTypeConverterFactory.standard())
                .withConversionSchema(ConversionSchemas.V2_COMPATIBLE)
                .withTableNameResolver { clazz, _ ->
                    val dynamoDBTable = clazz.getDeclaredAnnotation(DynamoDBTable::class.java)
                            ?: throw DynamoDBMappingException("$clazz not annotated with @DynamoDBTable")
                    return@withTableNameResolver tablePrefix + "-" + dynamoDBTable.tableName + "-" + tableSuffix
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