package com.kakao.jaypark.grapesticker.core.domain.converter

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


class LocalDateTimeConverter : DynamoDBTypeConverter<Date, LocalDateTime> {
    override fun convert(source: LocalDateTime): Date {
        return Date.from(source.atZone(ZoneId.systemDefault()).toInstant())
    }

    override fun unconvert(source: Date): LocalDateTime {
        return source.toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime()
    }
}