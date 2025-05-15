package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.data.Member
import kpn.api.common.data.Node
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.Way
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

import scala.jdk.CollectionConverters.IteratorHasAsScala

class RelationJsonDeserializer extends JsonDeserializer[Relation] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Relation = {
    val relationNode: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val members = readMembers(jsonParser, relationNode)
    val tags = readTags(relationNode)

    Relation(
      relationNode.get("id").asLong(),
      relationNode.get("version").asLong(),
      Json.objectMapper.treeToValue(relationNode.get("timestamp"), classOf[Timestamp]),
      relationNode.get("changeSetId").asLong(),
      tags,
      members
    )
  }

  private def readMembers(jsonParser: JsonParser, relationNode: JsonNode): Seq[Member] = {
    relationNode.get("members").iterator().asScala.toSeq.map { memberNode =>
      readMember(jsonParser, memberNode)
    }
  }

  private def readMember(jsonParser: JsonParser, memberNode: JsonNode): Member = {
    val role = readRole(memberNode)
    if (memberNode.has("way")) {
      val way = Json.objectMapper.treeToValue(memberNode.get("way"), classOf[Way])
      WayMember(way, role)
    }
    else if (memberNode.has("node")) {
      val node = Json.objectMapper.treeToValue(memberNode.get("node"), classOf[Node])
      NodeMember(node, role)
    }
    else if (memberNode.has("relation")) {
      val memberRelation = Json.objectMapper.treeToValue(memberNode.get("relation"), classOf[Relation])
      RelationMember(memberRelation, role)
    }
    else if (memberNode.has("relationId")) {
      val relationId = memberNode.get("relationId").asLong
      RelationIdMember(relationId, role)
    }
    else {
      throw JsonMappingException.from(
        jsonParser,
        "could not deserialize relation member"
      )
    }
  }

  private def readRole(member: JsonNode): Option[String] = {
    val roleNode = member.get("role")
    if (roleNode == null) {
      None
    }
    else {
      val text = roleNode.asText("")
      Option.when(text.nonEmpty) {
        text
      }
    }
  }

  private def readTags(relationNode: JsonNode): Seq[Tag] = {
    val tagsNode = relationNode.get("tags")
    tagsNode.elements().asScala.toSeq.map { n =>
      Json.objectMapper.treeToValue(n, classOf[Tag])
    }
  }
}
