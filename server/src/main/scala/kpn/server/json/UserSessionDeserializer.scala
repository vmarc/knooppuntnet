package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.common.UserSession

class UserSessionDeserializer extends JsonDeserializer[UserSession] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): UserSession = {

    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val _id = node.get("_id").textValue()
    val intervalSeconds = node.get("intervalSeconds").longValue()
    val createdMillis = node.get("createdMillis").longValue()
    val accessedMillis = node.get("accessedMillis").longValue()
    val expireAt = node.get("expireAt").get("$date").asText
    val principal = Option(node.get("principal").textValue())

    UserSession(
      _id,
      intervalSeconds,
      createdMillis,
      accessedMillis,
      expireAt,
      principal
    )
  }
}
