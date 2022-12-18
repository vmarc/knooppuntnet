package kpn.core.test

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tags
import kpn.core.data.Data
import kpn.core.data.DataBuilder

import scala.collection.mutable.ListBuffer

object TestData {

  def node(data: Data, id: Long): RawNode = {
    data.raw.nodeWithId(id).get
  }

  def relation(data: Data, id: Long): RawRelation = {
    data.raw.relationWithId(id).get
  }
}

class TestData() extends SharedTestObjects {

  private val nodes = ListBuffer[RawNode]()
  private val ways = ListBuffer[RawWay]()
  private val relations = ListBuffer[RawRelation]()

  def networkNode(id: Long, name: String = "", extraTags: Tags = Tags.empty): RawNode = {
    val n = newRawNodeWithName(id, name, extraTags)
    nodes += n
    n
  }

  def node(id: Long, tags: Tags = Tags.empty, latitude: String = "0", longitude: String = "0"): RawNode = {
    val n = newRawNode(id, latitude = latitude, longitude = longitude, tags = tags)
    nodes += n
    n
  }

  def way(id: Long, nodeIds: Long*): RawWay = {
    val w = newRawWay(id, tags = Tags.from("highway" -> "unclassified"), nodeIds = nodeIds.toVector)
    ways += w
    w
  }

  def way(id: Long, tags: Tags, nodeIds: Long*): RawWay = {
    val w = newRawWay(id, nodeIds = nodeIds.toVector, tags = tags)
    ways += w
    w
  }

  def relation(id: Long, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): RawRelation = {
    val relation = newRawRelation(id, members = members, tags = tags)
    relations += relation
    relation
  }

  def route(id: Long, name: String, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): RawRelation = {
    relation(
      id,
      members,
      Tags.from(
        "network" -> "rwn",
        "type" -> "route",
        "route" -> "foot",
        "note" -> name,
        "network:type" -> "node_network"
      ) ++ tags
    )
  }

  def networkRelation(id: Long, name: String, members: Seq[RawMember]): RawRelation = {
    relation(
      id,
      members,
      Tags.from(
        "network" -> "rwn",
        "type" -> "network",
        "name" -> name,
        "network:type" -> "node_network"
      )
    )
  }

  def rawData: RawData = {
    RawData(Some(defaultTimestamp), nodes.toSeq, ways.toSeq, relations.toSeq)
  }

  def data: Data = {
    new DataBuilder(rawData).data
  }
}


