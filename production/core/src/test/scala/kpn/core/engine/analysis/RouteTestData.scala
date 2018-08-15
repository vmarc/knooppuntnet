package kpn.core.engine.analysis

import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawData
import kpn.shared.data.raw.RawMember
import kpn.shared.data.raw.RawNode
import kpn.shared.data.raw.RawWay

import scala.collection.mutable.ListBuffer

class RouteTestData(val routeName: String, val networkType: NetworkType = NetworkType.hiking, val tags: Tags = Tags.empty) extends SharedTestObjects {

  private val nodeBuffer = ListBuffer[RawNode]()
  private val wayBuffer = ListBuffer[RawWay]()
  private val memberBuffer = ListBuffer[RawMember]()

  def routeRelationId = 1L

  def node(id: Long, name: String = "", lattitude: Double = 0, longitude: Double = 0): RawNode = {
    val tags = if (name == "") Tags.empty else Tags.from(networkType.nodeTagKey -> name)
    val n = newRawNode(id, lattitude.toString, longitude.toString, tags = tags)
    nodeBuffer += n
    n
  }

  def way(wayId: Long, nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    way(wayId, Tags.empty, nodeIds: _*)
  }

  def way(wayId: Long, tags: Tags, nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    val w = newRawWay(wayId, nodeIds = nodeIds, tags = tags)
    wayBuffer += w
    w
  }

  def memberNode(nodeId: Long, role: String = ""): RawMember = member("node", nodeId, role)

  def memberWay(wayId: Long, role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    memberWay(wayId, Tags.from("highway" -> "road"), role, nodeIds: _*)
  }

  def memberWay(wayId: Long, tags: Tags, role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    way(wayId, tags, nodeIds: _*)
    member("way", wayId, role)
  }

  def member(memberType: String, ref: Long, role: String = ""): RawMember = {
    val m = RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
    memberBuffer += m
    m
  }

  private def addNodesIfMissing(nodeIds: Seq[Long]): Unit = {
    val missingNodeIds = nodeIds.toSet -- nodeBuffer.map(_.id).toSet
    missingNodeIds.foreach(id => node(id))
  }

  def nodes: Seq[RawNode] = nodeBuffer

  def ways: Seq[RawWay] = wayBuffer

  def members: Seq[RawMember] = memberBuffer

  def data: Data = {
    val routeTags = Tags.from(
      "note" -> routeName,
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot"
    )
    val relation = newRawRelation(routeRelationId, members = memberBuffer, tags = routeTags)
    val rawData = RawData(None, nodeBuffer, wayBuffer, Seq(relation))
    new DataBuilder(networkType, rawData).data
  }
}
