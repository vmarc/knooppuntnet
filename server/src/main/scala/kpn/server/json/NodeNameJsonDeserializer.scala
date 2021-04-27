package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.NodeName
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType

class NodeNameJsonDeserializer extends JsonDeserializer[NodeName] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NodeName = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.get("name").asText
    val scopedNetworkTypeNode = node.get("scopedNetworkType")
    val rootNode = if (scopedNetworkTypeNode == null) node else scopedNetworkTypeNode
    val networkType = NetworkType.withName(rootNode.get("networkType").asText).get
    val networkScope = NetworkScope.withName(rootNode.get("networkScope").asText).get
    NodeName(networkType, networkScope, name)
  }
}
