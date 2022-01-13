package kpn.core.test

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser

import scala.xml.InputSource
import scala.xml.XML

object OverpassData {
  def empty: OverpassData = {
    OverpassData()
  }

  def load(filename: String): OverpassData = {
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser().parse(xml.head)
    OverpassData(
      rawData.nodes,
      rawData.ways,
      rawData.relations
    )
  }
}

case class OverpassData(
  nodes: Seq[RawNode] = Seq.empty,
  ways: Seq[RawWay] = Seq.empty,
  relations: Seq[RawRelation] = Seq.empty
) extends SharedTestObjects {

  def networkNode(id: Long, name: String = "", extraTags: Tags = Tags.empty, version: Long = 0): OverpassData = {
    val n = newRawNode(id, tags = newNodeTags(name) ++ extraTags, version = version)
    copy(nodes = nodes :+ n)
  }

  def node(
    id: Long,
    tags: Tags = Tags.empty,
    latitude: String = "0",
    longitude: String = "0",
    version: Long = 0,
    timestamp: Timestamp = defaultTimestamp
  ): OverpassData = {
    val n = newRawNode(
      id,
      latitude = latitude,
      longitude = longitude,
      version = version,
      tags = tags,
      timestamp = timestamp
    )
    copy(nodes = nodes :+ n)
  }

  def way(id: Long, nodeIds: Long*): OverpassData = {
    val w = newRawWay(id, nodeIds = nodeIds, tags = Tags.from("highway" -> "unclassified"))
    copy(ways = ways :+ w)
  }

  def relation(id: Long, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty, version: Long = 0): OverpassData = {
    val relation = newRawRelation(id, members = members, tags = tags, version = version)
    copy(relations = relations :+ relation)
  }

  def route(id: Long, name: String, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty, version: Long = 0): OverpassData = {
    relation(id, members, newRouteTags(name) ++ tags, version)
  }

  def networkRelation(id: Long, name: String, members: Seq[RawMember] = Seq.empty, version: Long = 0): OverpassData = {
    relation(id, members, newNetworkTags(name), version)
  }

  def rawData: RawData = {
    RawData(Some(defaultTimestamp), nodes, ways, relations)
  }

  def rawNodeWithId(nodeId: Long): RawNode = {
    nodes.find(_.id == nodeId).getOrElse(
      throw new IllegalArgumentException(s"No node with id $nodeId in test data")
    )
  }

  def rawRelationWithId(relationId: Long): RawRelation = {
    relations.find(_.id == relationId).getOrElse(
      throw new IllegalArgumentException(s"No relation with id $relationId in test data")
    )
  }

  def data: Data = {
    new DataBuilder(rawData).data
  }
}
