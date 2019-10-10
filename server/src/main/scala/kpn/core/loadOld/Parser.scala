package kpn.core.loadOld

import kpn.core.common.TimestampUtil
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay
import kpn.shared.Timestamp
import kpn.shared.data.Tag
import kpn.shared.data.Tags

class Parser() {

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
      val version = (n \ "@version").text.toInt
      val timestamp = timestampIn(n)
      val changeSetId = (n \ "@changeset").text.toLong
      val tags = tagsIn(n)
      RawNode(id, latitude, longitude, version, timestamp, changeSetId, tags)
    }
  }

  private def waysIn(xml: scala.xml.Node): Seq[RawWay] = {
    (xml \ "way").map { w =>
      val id = idIn(w)
      val version = (w \ "@version").text.toInt
      val timestamp = timestampIn(w)
      val changeSetId = (w \ "@changeset").text.toLong
      val nodeIds = (w \ "nd").map(t => (t \ "@ref").text.toLong)
      val tags = tagsIn(w)
      RawWay(id, version, timestamp, changeSetId, nodeIds, tags)
    }
  }

  private def relationsIn(xml: scala.xml.Node): Seq[RawRelation] = {
    (xml \ "relation").map { r =>
      val id = idIn(r)
      val version = (r \ "@version").text.toInt
      val timestamp = timestampIn(r)
      val changeSetId = (r \ "@changeset").text.toLong
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
