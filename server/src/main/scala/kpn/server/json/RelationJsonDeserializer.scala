package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation

import scala.jdk.CollectionConverters.IteratorHasAsScala

class RelationJsonDeserializer extends JsonDeserializer[Relation] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Relation = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val raw = Json.objectMapper.treeToValue(node.get("raw"), classOf[RawRelation])

    val members = node.get("members").iterator().asScala.toSeq.map { member =>
      val role = Json.objectMapper.treeToValue(member.get("role"), classOf[Option[String]])
      if (member.has("way")) {
        val way = Json.objectMapper.treeToValue(member.get("way"), classOf[Way])
        WayMember(way, role)
      }
      else if (member.has("node")) {
        val node = Json.objectMapper.treeToValue(member.get("node"), classOf[Node])
        NodeMember(node, role)
      }
      else if (member.has("relation")) {
        val memberRelation = Json.objectMapper.treeToValue(member.get("relation"), classOf[Relation])
        RelationMember(memberRelation, role)
      }
      else {
        throw JsonMappingException.from(
          jsonParser,
          "could not deserialize relation member"
        )
      }
    }

    Relation(raw, members)
  }
}
