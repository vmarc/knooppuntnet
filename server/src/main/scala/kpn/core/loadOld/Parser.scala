package kpn.core.loadOld

import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.common.TimestampUtil

class Parser(full: Boolean = true) {

  private val now = Time.now

  def parse(xml: scala.xml.Node): RawData = {
    val timestamp = {
      val string = ((xml \\ "meta") \ "@osm_base").text
      if (string.nonEmpty) {
        Some(TimestampUtil.parseIso(string))
      }
      else {
        None
      }
    }
    val nodes = nodesIn(xml)
    val ways = waysIn(xml)
    val relations = relationsIn(xml)
    RawData(timestamp, nodes, ways, relations)
  }

  private def nodesIn(xml: scala.xml.Node): Seq[RawNode] = {
    (xml \ "node").map { n =>
      val id = idIn(n)
      val latitude = (n \ "@lat").text
      val longitude = (n \ "@lon").text
      val version = if (full) (n \ "@version").text.toInt else 0
      val timestamp = if (full) timestampIn(n) else now
      val changeSetId = if (full) (n \ "@changeset").text.toLong else 0
      val tags = tagsIn(n)
      RawNode(id, latitude, longitude, version, timestamp, changeSetId, tags)
    }
  }

  private def waysIn(xml: scala.xml.Node): Seq[RawWay] = {
    (xml \ "way").map { w =>
      val id = idIn(w)
      val version = if (full) (w \ "@version").text.toInt else 0
      val timestamp = if (full) timestampIn(w) else now
      val changeSetId = if (full) (w \ "@changeset").text.toLong else 0
      val nodeIds = (w \ "nd").map(t => (t \ "@ref").text.toLong)
      val tags = tagsIn(w)
      RawWay(id, version, timestamp, changeSetId, nodeIds.toVector, tags)
    }
  }

  private def relationsIn(xml: scala.xml.Node): Seq[RawRelation] = {
    (xml \ "relation").map { r =>
      val id = idIn(r)
      val version = if (full) (r \ "@version").text.toInt else 0
      val timestamp = if (full) timestampIn(r) else now
      val changeSetId = if (full) (r \ "@changeset").text.toLong else 0
      val members = (r \ "member").map { t =>
        val memberType = (t \ "@type").text
        val ref = (t \ "@ref").text.toLong
        val role = (t \ "@role").text
        val roleOption = if (role == "") None else Some(role)
        RawMember(memberType, ref, roleOption)
      }
      val tags = tagsIn(r)
      RawRelation(id, version, timestamp, changeSetId, members, tags)
    }
  }

  private def timestampIn(xml: scala.xml.Node): Timestamp = {
    TimestampUtil.parseIso((xml \ "@timestamp").text)
  }

  private def tagsIn(xml: scala.xml.Node): Tags = {
    Tags(
      (xml \ "tag").map { t =>
        val key = (t \ "@k").text
        val value = (t \ "@v").text
        Tag(key, value)
      }
    )
  }

  private def idIn(xml: scala.xml.Node): Long = (xml \ "@id").text.toLong
}
