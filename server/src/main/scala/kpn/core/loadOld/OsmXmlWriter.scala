package kpn.core.loadOld

import java.io.PrintWriter

import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.Xml

class OsmXmlWriter(out: PrintWriter, full: Boolean = true) {

  def printHeader(): Unit = {
    out.println("""<?xml version="1.0" encoding="UTF-8"?>""")
    out.println("""<osm version="0.6">""")
  }

  def printFooter(): Unit = {
    out.println("</osm>")
  }

  def printNode(node: RawNode): Unit = {
    val id = node.id
    val version = node.version
    val lat = node.latitude
    val lon = node.longitude
    val timestamp = node.timestamp.iso
    val changeset = node.changeSetId

    if (full) {
      out.print(s"""  <node id="$id" version="$version" lat="$lat" lon="$lon" timestamp="$timestamp" changeset="$changeset"""")
    }
    else {
      out.print(s"""  <node id="$id" lat="$lat" lon="$lon"""")
    }

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
    if (full) {
      out.println(s"""  <way id="$id" version="$version" timestamp="$timestamp" changeset="$changeset">""")
    }
    else {
      out.println(s"""  <way id="$id">""")
    }
    printTags(way.tags)
    printNodeReferences(way.nodeIds)
    out.println("  </way>")
  }

  def printRelation(relation: RawRelation): Unit = {
    val id = relation.id
    val version = relation.version
    val timestamp = relation.timestamp.iso
    val changeset = relation.changeSetId
    if (full) {
      out.println(s"""  <relation id="$id" version="$version" timestamp="$timestamp" changeset="$changeset">""")
    }
    else {
      out.println(s"""  <relation id="$id">""")
    }
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
    tags.tags.foreach { case Tag(key, value) =>
      out.println(s"""    <tag k="$key" v="${Xml.escape(value)}"/>""")
    }
  }

  def close(): Unit = {
    out.close()
  }
}
