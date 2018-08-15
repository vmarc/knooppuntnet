package kpn.core.loadOld

import java.io.PrintWriter

import kpn.core.util.Xml
import kpn.shared.data.Tag
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay

class OsmXmlWriter(out: PrintWriter) {

  def printHeader(): Unit = {
    out.println( """<?xml version="1.0" encoding="UTF-8"?>""")
    out.println( """<osm version="0.6">""")
  }

  def printFooter(): Unit = {
    out.println("</osm>")
  }

  def printNode(node: RawNode): Unit = {
    val id = node.id
    val version = node.version
    val lat = node.latitude.toString
    val lon = node.longitude.toString
    val timestamp = node.timestamp.iso
    val changeset = node.changeSetId

    out.print(s"""  <node id="$id" version="$version" lat="$lat" lon="$lon" timestamp="$timestamp" changeset="$changeset"""")

    if (node.tags.isEmpty) {
      out.println("/>")
    }
    else {
      out.println(">")
      printTags(node.tags)
      out.println("  </node>")
    }
  }

  def printWay(way: RawWay): Unit = {
    val id = way.id
    val version = way.version
    val timestamp = way.timestamp.iso
    val changeset = way.changeSetId
    out.println(s"""  <way id="$id" version="$version" timestamp="$timestamp" changeset="$changeset">""")
    printTags(way.tags)
    printNodeReferences(way.nodeIds)
    out.println("  </way>")
  }

  def printRelation(relation: RawRelation): Unit = {
    val id = relation.id
    val version = relation.version
    val timestamp = relation.timestamp.iso
    val changeset = relation.changeSetId
    out.println(s"""  <relation id="$id" version="$version" timestamp="$timestamp" changeset="$changeset">""")
    printMembers(relation.members)
    printTags(relation.tags)
    out.println("  </relation>")
  }

  private def printNodeReferences(nodeIds: Seq[Long]): Unit = {
    nodeIds.foreach { nodeId =>
      out.println(s"""    <nd ref="$nodeId"/>""")
    }
  }

  private def printMembers(members: Seq[RawMember]): Unit = {
    members.foreach { member =>
      val memberType = member.memberType
      val ref = member.ref
      val role = Xml.escape(member.role.getOrElse(""))
      out.println(s"""    <member type="$memberType" ref="$ref" role="$role"/>""")
    }
  }

  private def printTags(tags: Tags): Unit = {
    tags.tags.foreach { case (Tag(key, value)) =>
      out.println(s"""    <tag k="$key" v="${Xml.escape(value)}"/>""")
    }
  }

  def close(): Unit = {
    out.close()
  }
}
