package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.base.ObjectId

class ObjectIdJsonDeserializer extends JsonDeserializer[ObjectId] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ObjectId = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val oid = node.get("$oid").textValue()
    ObjectId(oid)
  }
}
