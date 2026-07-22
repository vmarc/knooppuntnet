package kpn.core.loadOld

import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.api.time.Time
import kpn.api.time.TimestampUtil

class Parser(includeMetadata: Boolean = true) {
  private val now = Time.now

  def parse(xml: scala.xml.Node): RawData = {
    val timestamp = extractTimestamp(xml)
    val nodes = exractNodes(xml)
    val ways = extractWays(xml)
    val relations = extractRelations(xml)
    RawData(
      timestamp,
      nodes,
      ways,
      relations
    )
  }

  private def extractTimestamp(xml: scala.xml.Node): Option[Timestamp] = {
    val string = ((xml \\ "meta") \ "@osm_base").text
    Option.when(string.nonEmpty) {
      TimestampUtil.parseIso(string)
    }
  }

  private def exractNodes(xml: scala.xml.Node): Seq[RawNode] = {
    (xml \ "node").map { n =>
      val id = extractId(n)
      val latitude = extractAttribute(n, "@lat")
      val longitude = extractAttribute(n, "@lon")
      val meta = extractMetadata(n)
      val tags = tagsIn(n)
      RawNode(
        id,
        latitude,
        longitude,
        meta.version,
        meta.timestamp,
        meta.changeSetId,
        tags
      )
    }
  }

  private def extractWays(xml: scala.xml.Node): Seq[RawWay] = {
    (xml \ "way").map { w =>
      val id = extractId(w)
      val meta = extractMetadata(w)
      val nodeIds = (w \ "nd").map(t => extractLong(t, "@ref")).toVector
      val tags = tagsIn(w)
      RawWay(
        id,
        meta.version,
        meta.timestamp,
        meta.changeSetId,
        nodeIds,
        tags
      )
    }
  }

  private def extractRelations(xml: scala.xml.Node): Seq[RawRelation] = {
    (xml \ "relation").map { relation =>
      val id = extractId(relation)
      val meta = extractMetadata(relation)
      val members = (relation \ "member").map { member =>
        val memberType = MemberType.withName(extractAttribute(member, "@type"))
        val ref = extractLong(member, "@ref")
        val role = extractAttribute(member, "@role")
        val roleOption = if (role.isEmpty) None else Some(role)
        RawMember(memberType, ref, roleOption)
      }
      val tags = tagsIn(relation)
      RawRelation(
        id,
        meta.version,
        meta.timestamp,
        meta.changeSetId,
        members,
        tags
      )
    }
  }

  private def extractMetadata(xml: scala.xml.Node): MetaData = {
    if (includeMetadata) {
      val version = extractLong(xml, "@version")
      val timestamp = timestampIn(xml)
      val changeSetId = extractLong(xml, "@changeset")
      MetaData(version, timestamp, changeSetId)
    } else {
      MetaData(0L, now, 0L)
    }
  }

  private def timestampIn(xml: scala.xml.Node): Timestamp = {
    TimestampUtil.parseIso(extractAttribute(xml, "@timestamp"))
  }

  private def tagsIn(xml: scala.xml.Node): Seq[Tag] = {
    (xml \ "tag").map { t =>
      val key = extractAttribute(t, "@k")
      val value = extractAttribute(t, "@v")
      Tag(key, value)
    }
  }

  private def extractId(xml: scala.xml.Node): Long = {
    extractLong(xml, "@id")
  }

  private def extractLong(xml: scala.xml.Node, attribute: String): Long = {
    extractAttribute(xml, attribute).toLong
  }

  private def extractAttribute(xml: scala.xml.Node, attribute: String): String = {
    (xml \ attribute).text
  }
}
