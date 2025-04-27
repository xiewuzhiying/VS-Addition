package io.github.xiewuzhiying.vs_addition.context.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.joml.primitives.AABBdc
import java.io.IOException


class AABBdcSerializer : JsonSerializer<AABBdc>() {
    @Throws(IOException::class)
    override fun serialize(value: AABBdc, gen: JsonGenerator, serializers: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField("minX", value.minX())
        gen.writeNumberField("minY", value.minY())
        gen.writeNumberField("minZ", value.minZ())
        gen.writeNumberField("maxX", value.maxX())
        gen.writeNumberField("maxY", value.maxY())
        gen.writeNumberField("maxZ", value.maxZ())
        gen.writeEndObject()
    }
}
