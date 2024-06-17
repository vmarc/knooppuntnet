package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.data.raw.RawRelation
import kpn.core.tools.next.domain.OldNode
import kpn.core.tools.next.domain.OldNodeMember
import kpn.core.tools.next.domain.OldRelation
import kpn.core.tools.next.domain.OldRelationMember
import kpn.core.tools.next.domain.OldWay
import kpn.core.tools.next.domain.OldWayMember

import scala.jdk.CollectionConverters.IteratorHasAsScala

class OldRelationJsonDeserializer extends JsonDeserializer[OldRelation] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): OldRelation = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val raw = Json.objectMapper.treeToValue(node.get("raw"), classOf[RawRelation])

    val members = node.get("members").iterator().asScala.toSeq.map { member =>
      val role = Json.objectMapper.treeToValue(member.get("role"), classOf[Option[String]])
      if (member.has("way")) {
        val way = Json.objectMapper.treeToValue(member.get("way"), classOf[OldWay])
        OldWayMember(way, role)
      }
      else if (member.has("node")) {
        val node = Json.objectMapper.treeToValue(member.get("node"), classOf[OldNode])
        OldNodeMember(node, role)
      }
      else if (member.has("relation")) {
        val memberRelation = Json.objectMapper.treeToValue(member.get("relation"), classOf[OldRelation])
        OldRelationMember(memberRelation, role)
      }
      else {
        throw JsonMappingException.from(
          jsonParser,
          "could not deserialize relation member"
        )
      }
    }

    OldRelation(raw, members)
  }
}
