package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.NetworkScope
import kpn.api.common.NodeName
import kpn.api.common.RouteType

class NodeNameJsonDeserializer extends JsonDeserializer[NodeName] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NodeName = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.get("name").asText
    val scopedRouteTypeNode = node.get("scopedRouteType")
    val rootNode = if (scopedRouteTypeNode == null) node else scopedRouteTypeNode
    val routeType = RouteType.withName(rootNode.get("routeType").asText)
    val networkScope = NetworkScope.withName(rootNode.get("networkScope").asText)
    val longName = Option.apply(node.get("longName")).map(_.asText)
    val proposedNode = node.get("proposed")
    val proposed = if (proposedNode == null) false else proposedNode.asBoolean()
    NodeName(routeType, networkScope, name, longName, proposed)
  }
}
