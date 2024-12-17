package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.data.Data
import kpn.core.data.DataBuilder

import scala.collection.mutable.ListBuffer

class RouteTestData(
  val routeName: String,
  val scopedNetworkType: ScopedNetworkType = ScopedNetworkType.rwn,
  val routeTags: Seq[Tag] = Seq.empty
) extends SharedTestObjects {

  private val nodeBuffer = ListBuffer[RawNode]()
  private val wayBuffer = ListBuffer[RawWay]()
  private val relationBuffer = ListBuffer[RawRelation]()
  private val memberBuffer = ListBuffer[RawMember]()

  def routeRelationId = 1L

  def node(id: Long, name: String = "", lattitude: Double = 0, longitude: Double = 0): RawNode = {
    val tags = if (name == "") Seq.empty else Tags.from(scopedNetworkType.nodeRefTagKey -> name, "network:type" -> "node_network")
    rawNode(newRawNode(id, lattitude.toString, longitude.toString, tags = tags))
  }

  def rawNode(rawNode: RawNode): RawNode = {
    nodeBuffer += rawNode
    rawNode
  }

  def way(wayId: Long, nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    way(wayId, Seq.empty, nodeIds: _*)
  }

  def way(wayId: Long, tags: Seq[Tag], nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    val w = newRawWay(wayId, nodeIds = nodeIds.toVector, tags = tags)
    wayBuffer += w
    w
  }

  def relation(relationId: Long): RawRelation = {
    val r = newRawRelation(relationId)
    relationBuffer += r
    r
  }

  def memberNode(nodeId: Long, role: String = ""): RawMember = member(MemberType.Node, nodeId, role)

  def memberWay(wayId: Long, role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    memberWay(wayId, Tags.from("highway" -> "road"), role, nodeIds: _*)
  }

  def memberWay(wayId: Long, tags: Seq[Tag], role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    way(wayId, tags, nodeIds: _*)
    member(MemberType.Way, wayId, role)
  }

  def member(memberType: MemberType, ref: Long, role: String = ""): RawMember = {
    val m = RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
    memberBuffer += m
    m
  }

  private def addNodesIfMissing(nodeIds: Seq[Long]): Unit = {
    val missingNodeIds = nodeIds.toSet -- nodeBuffer.map(_.id).toSet
    missingNodeIds.foreach(id => node(id))
  }

  def nodes: Seq[RawNode] = nodeBuffer.toSeq

  def ways: Seq[RawWay] = wayBuffer.toSeq

  def members: Seq[RawMember] = memberBuffer.toSeq

  def data: Data = {

    val routeNameTags = if (routeName.nonEmpty) {
      Tags.from("ref" -> routeName)
    }
    else {
      Seq.empty
    }

    val standardRouteTags = Tags.from(
      "network" -> scopedNetworkType.key,
      "type" -> "route",
      "route" -> "foot",
      "network:type" -> "node_network"
    )
    val allRouteTags = routeNameTags ++ routeTags ++ standardRouteTags
    val relation = newRawRelation(routeRelationId, members = memberBuffer.toSeq, tags = allRouteTags)
    val rawData = RawData(None, nodeBuffer.toSeq, wayBuffer.toSeq, Seq(relation) ++ relationBuffer)
    new DataBuilder(rawData).data
  }
}
