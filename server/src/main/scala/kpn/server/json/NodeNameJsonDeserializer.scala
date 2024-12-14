package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.NetworkScope
import kpn.api.common.NetworkType
import kpn.api.common.NodeName

class NodeNameJsonDeserializer extends JsonDeserializer[NodeName] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NodeName = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.get("name").asText
    val scopedNetworkTypeNode = node.get("scopedNetworkType")
    val rootNode = if (scopedNetworkTypeNode == null) node else scopedNetworkTypeNode
    val networkType = NetworkType.withName(rootNode.get("networkType").asText)
    val networkScope = NetworkScope.withName(rootNode.get("networkScope").asText)
    val longName = Option.apply(node.get("longName")).map(_.asText)
    val proposedNode = node.get("proposed")
    val proposed = if (proposedNode == null) false else proposedNode.asBoolean()
    NodeName(networkType, networkScope, name, longName, proposed)
  }
}
