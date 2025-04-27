package io.github.xiewuzhiying.vs_addition.context.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.joml.primitives.AABBd
import org.joml.primitives.AABBdc
import java.io.IOException


class AABBdcDeserializer : JsonDeserializer<AABBdc>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): AABBdc {
        val node = p.codec.readTree<JsonNode>(p)
        val minX = node["minX"].asDouble()
        val minY = node["minY"].asDouble()
        val minZ = node["minZ"].asDouble()
        val maxX = node["maxX"].asDouble()
        val maxY = node["maxY"].asDouble()
        val maxZ = node["maxZ"].asDouble()
        return AABBd(minX, minY, minZ, maxX, maxY, maxZ)
    }
}
