package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.shared.NetworkType
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class NetworkTypeJsonDeserializer extends JsonDeserializer[NetworkType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NetworkType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    NetworkType.withName(node.asText) match {
      case Some(networkType) => networkType
      case None => throw new RuntimeException("Could not deserialize network type")
    }
  }
}
