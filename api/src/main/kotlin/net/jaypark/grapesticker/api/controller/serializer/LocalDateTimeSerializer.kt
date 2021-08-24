package net.jaypark.grapesticker.api.controller.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    val defaultFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {

        val string: String = decoder.decodeString().trim { it <= ' ' }
        if (string.length > 10 && string[10] == 'T') {
            return if (string.endsWith("Z")) {
                LocalDateTime.ofInstant(Instant.parse(string), ZoneOffset.UTC)
            } else {
                LocalDateTime.parse(string, defaultFormatter)
            }
        }
        return LocalDateTime.parse(string, defaultFormatter)
    }

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        return encoder.encodeString(value.format(defaultFormatter))
    }

}
