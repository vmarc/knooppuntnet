package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

class SubsetJsonDeserializer extends JsonDeserializer[Subset] {

  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Subset = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    if (node.isObject) {
      // regular structure
      val country = jsonParser.getCodec.treeToValue(node.get("country"), classOf[Country])
      val networkType = jsonParser.getCodec.treeToValue(node.get("networkType"), classOf[NetworkType])
      Subset(country, networkType)
    }
    else {
      // backward compatible structure
      val names = node.asText.split(":")
      Subset.ofName(names.head, names(1)).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Subset expected"
        )
      )
    }
  }
}
