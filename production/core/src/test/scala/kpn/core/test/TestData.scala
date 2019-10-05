package kpn.core.test

import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.Timestamp
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawRelation
import kpn.shared.data.raw.RawWay

import scala.collection.mutable.ListBuffer

object TestData {

  private val defaultTimestamp = Timestamp(2015, 8, 11)

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
    val w = newRawWay(id, tags = Tags.from("highway" -> "unclassified"), nodeIds = nodeIds)
    ways += w
    w
  }

  def way(id: Long, tags: Tags, nodeIds: Long*): RawWay = {
    val w = newRawWay(id, nodeIds = nodeIds, tags = tags)
    ways += w
    w
  }

  def relation(id: Long, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): RawRelation = {
    val relation = newRawRelation(id, members = members, tags = tags)
    relations += relation
    relation
  }

  def route(id: Long, name: String, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): RawRelation = {
    relation(id, members, Tags.from("network" -> "rwn", "type" -> "route", "route" -> "foot", "note" -> name) ++ tags)
  }

  def networkRelation(id: Long, name: String, members: Seq[RawMember]): RawRelation = {
    relation(id, members, Tags.from("network" -> "rwn", "type" -> "network", "name" -> name))
  }

  def rawData: RawData = {
    RawData(Some(defaultTimestamp), nodes, ways, relations)
  }

  def data: Data = {
    new DataBuilder(NetworkType.hiking, rawData).data
  }

}


case class TestData2(
  nodes: Seq[RawNode] = Seq.empty,
  ways: Seq[RawWay] = Seq.empty,
  relations: Seq[RawRelation] = Seq.empty
) extends SharedTestObjects {

  def networkNode(id: Long, name: String = "", extraTags: Tags = Tags.empty): TestData2 = {
    val n = newRawNode(id, tags =  newNodeTags(name) ++ extraTags)
    copy(nodes = nodes :+ n)
  }

  def foreignNetworkNode(id: Long, name: String, extraTags: Tags = Tags.empty): TestData2 = {
    val n = newRawNode(id, latitude = "99", longitude = "99", tags = Tags.from("rwn_ref" -> name, "network:type" -> "node_network") ++ extraTags)
    copy(nodes = nodes :+ n)
  }

  def node(id: Long, tags: Tags = Tags.empty, latitude: String = "0", longitude: String = "0"): TestData2 = {
    val n = newRawNode(id, latitude = latitude, longitude = longitude, tags = tags)
    copy(nodes = nodes :+ n)
  }

  def way(id: Long, nodeIds: Long*): TestData2 = {
    val w = newRawWay(id, nodeIds = nodeIds, tags = Tags.from("highway" -> "unclassified"))
    copy(ways = ways :+ w)
  }

  def relation(id: Long, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): TestData2 = {
    val relation = newRawRelation(id, members = members, tags = tags)
    copy(relations = relations :+ relation)
  }

  def route(id: Long, name: String, members: Seq[RawMember] = Seq.empty, tags: Tags = Tags.empty): TestData2 = {
    relation(id, members, newRouteTags(name) ++ tags)
  }

  def networkRelation(id: Long, name: String, members: Seq[RawMember] = Seq.empty): TestData2 = {
    relation(id, members, newNetworkTags(name))
  }

  def rawData: RawData = {
    RawData(Some(defaultTimestamp), nodes, ways, relations)
  }

  def data: Data = {
    new DataBuilder(NetworkType.hiking, rawData).data
  }

}

