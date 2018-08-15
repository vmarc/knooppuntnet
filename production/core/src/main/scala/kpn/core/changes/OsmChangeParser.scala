package kpn.core.changes

import kpn.core.common.TimestampUtil
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay
import kpn.shared.Timestamp
import kpn.shared.changes.Change
import kpn.shared.changes.ChangeAction
import kpn.shared.data.Tag
import kpn.shared.data.Tags

class OsmChangeParser {

  def parse(xml: scala.xml.Node): OsmChange = {

    val actions = xml.child.filter(isAction).map { actionXml =>
      val action = actionXml.label match {
        case "create" => ChangeAction.Create
        case "modify" => ChangeAction.Modify
        case "delete" => ChangeAction.Delete
      }
      val elements = actionXml.child.filter(isElement).map { element =>
        element.label match {
          case "node" => node(element)
          case "way" => way(element)
          case "relation" => relation(element)
        }
      }
      Change(action, elements)
    }
    OsmChange(actions)
  }

  private def node(node: scala.xml.Node): RawNode = {
    val latitude = (node \ "@lat").text
    val longitude = (node \ "@lon").text
    RawNode(id(node), latitude, longitude, version(node), timestamp(node), changeSetId(node), tags(node))
  }

  def way(node: scala.xml.Node): RawWay = {
    val wayNodeIds = (node \ "nd").map { t => (t \ "@ref").text.toLong}
    RawWay(id(node), version(node), timestamp(node), changeSetId(node), wayNodeIds, tags(node))
  }

  private def relation(node: scala.xml.Node): RawRelation = {
    val members = (node \ "member").map { member =>
      val memberType = (member \ "@type").text
      val ref = (member \ "@ref").text.toLong
      val role = (member \ "@role").text
      val roleOption = if (role == "") None else Some(role)
      RawMember(memberType, ref, roleOption)
    }
    RawRelation(id(node), version(node), timestamp(node), changeSetId(node), members, tags(node))
  }

  private def tags(node: scala.xml.Node): Tags = {
    Tags(
      (node \ "tag").map { tag =>
        val key = (tag \ "@k").text
        val value = (tag \ "@v").text
        Tag(key, value)
      }
    )
  }

  private def id(node: scala.xml.Node): Long = (node \ "@id").text.toLong

  private def version(node: scala.xml.Node): Int = (node \ "@version").text.toInt

  private def timestamp(node: scala.xml.Node): Timestamp = TimestampUtil.parseIso((node \ "@timestamp").text)

  private def changeSetId(node: scala.xml.Node): Long = (node \ "@changeset").text.toLong

  private def isAction(xml: scala.xml.Node): Boolean = Seq("create", "modify", "delete").contains(xml.label)

  private def isElement(xml: scala.xml.Node): Boolean = Seq("node", "way", "relation").contains(xml.label)
}
