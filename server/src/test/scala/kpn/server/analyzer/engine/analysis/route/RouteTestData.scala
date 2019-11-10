package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.data.Data
import kpn.core.data.DataBuilder
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.ScopedNetworkType

import scala.collection.mutable.ListBuffer

class RouteTestData(val routeName: String, val networkType: NetworkType = NetworkType.hiking, val tags: Tags = Tags.empty) extends SharedTestObjects {

  private val nodeBuffer = ListBuffer[RawNode]()
  private val wayBuffer = ListBuffer[RawWay]()
  private val memberBuffer = ListBuffer[RawMember]()

  def routeRelationId = 1L

  def node(id: Long, name: String = "", lattitude: Double = 0, longitude: Double = 0): RawNode = {
    val tags = if (name == "") Tags.empty else Tags.from(ScopedNetworkType(NetworkScope.regional, networkType).nodeTagKey -> name, "network:type" -> "node_network")
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
    val standardRouteTags = Tags.from(
      "note" -> routeName,
      "network" -> "rwn",
      "type" -> "route",
      "route" -> "foot",
      "network:type" -> "node_network"
    )
    val routeTags = Tags(tags.tags ++ standardRouteTags.tags)
    val relation = newRawRelation(routeRelationId, members = memberBuffer, tags = routeTags)
    val rawData = RawData(None, nodeBuffer, wayBuffer, Seq(relation))
    new DataBuilder(rawData).data
  }
}
