package com.kakao.jaypark.grapesticker.config

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer
import com.kakao.jaypark.grapesticker.utils.AwsDynamoDbLocalTestUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Configuration
@Profile("test")
@ConditionalOnProperty(name = ["embedded-dynamodb.use"], havingValue = "true")
class EmbeddedDynamoDBConfig {
    private var server: DynamoDBProxyServer? = null
    @PostConstruct
    fun start() {
        if (server != null) {
            return
        }
        try {
            AwsDynamoDbLocalTestUtils.initSqLite()
            server = ServerRunner.createServerFromCommandLineArgs(arrayOf("-inMemory"))
            server?.start()
//            log.info("Start Embedded DynamoDB")
        } catch (e: Exception) {
            throw IllegalStateException("Fail Start Embedded DynamoDB", e)
        }
    }

    @PreDestroy
    fun stop() {
        if (server == null) {
            return
        }
        try {
//            log.info("Stop Embedded DynamoDB")
            server?.stop()
        } catch (e: Exception) {
            throw IllegalStateException("Fail Stop Embedded DynamoDB", e)
        }
    }
}