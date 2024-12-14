package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.NetworkType
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder

import scala.collection.mutable.ListBuffer

class StructureTestSetupBuilder extends SharedTestObjects {

  private val nodeBuffer = ListBuffer[RawNode]()
  private val wayBuffer = ListBuffer[RawWay]()
  private val memberBuffer = ListBuffer[RawMember]()

  def memberWay(wayId: Long, role: String, nodeIds: Long*): RawMember = {
    memberWayWithTags(wayId, role, Tags.from("highway" -> "road"), nodeIds: _*)
  }

  def memberWayWithTags(wayId: Long, role: String, tags: Seq[Tag], nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    memberWay(wayId, tags, role, nodeIds: _*)
  }

  def memberRoundabout(wayId: Long, role: String, nodeIds: Long*): RawMember = {
    memberWayWithTags(wayId, role, Tags.from("highway" -> "road", "junction" -> "roundabout"), nodeIds: _*)
  }

  def node(id: Long, name: String = "", lattitude: Double = 0, longitude: Double = 0): RawNode = {
    rawNode(
      newRawNode(
        id,
        lattitude.toString,
        longitude.toString,
        tags = Tags.from(
          "network:type" -> "node_network",
          "rwn_ref" -> name,
        )
      )
    )
  }

  def nodeWithTags(id: Long, tags: Seq[Tag]): RawNode = {
    rawNode(
      newRawNode(
        id,
        "0",
        "0",
        tags = tags
      )
    )
  }

  private def rawNode(rawNode: RawNode): RawNode = {
    nodeBuffer += rawNode
    rawNode
  }

  private def way(wayId: Long, nodeIds: Long*): RawWay = {
    way(wayId, Seq.empty, nodeIds: _*)
  }

  private def way(wayId: Long, tags: Seq[Tag], nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    val w = newRawWay(wayId, nodeIds = nodeIds.toVector, tags = tags)
    wayBuffer += w
    w
  }

  private def memberWay(wayId: Long, tags: Seq[Tag], role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    way(wayId, tags, nodeIds: _*)
    member("way", wayId, role)
  }

  def memberNode(nodeId: Long, role: String = ""): RawMember = {
    member("node", nodeId, role)
  }

  def roundAboutTags = Tags.from("highway" -> "road", "junction" -> "roundabout")

  private def member(memberType: String, ref: Long, role: String = ""): RawMember = {
    val m = RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
    memberBuffer += m
    m
  }

  private def addNodesIfMissing(nodeIds: Seq[Long]): Unit = {
    val missingNodeIds = nodeIds.toSet -- nodeBuffer.map(_.id).toSet
    missingNodeIds.foreach { id =>
      rawNode(newRawNode(id))
    }
  }

  def build: StructureTestSetup = {
    val relation = newRawRelation(
      1,
      members = memberBuffer.toSeq,
      tags = Tags.from(
        "name" -> "name",
        "type" -> "route",
        "route" -> "hiking",
      )
    )
    val rawData = RawData(None, nodeBuffer.toSeq, wayBuffer.toSeq, Seq(relation))
    new StructureTestSetup(new DataBuilder(rawData).data)
  }

  def build(
    from: String,
    to: String,
    scopedNetworkType: ScopedNetworkType = ScopedNetworkType.rwn,
    routeTags: Seq[Tag] = Seq.empty
  ): StructureTestSetup = {
    val routeTagValue = if (scopedNetworkType.networkType == NetworkType.cycling) "bicycle" else "hiking"
    val relation = newRawRelation(
      1,
      members = memberBuffer.toSeq,
      tags = Tags.from(
        "from" -> from,
        "to" -> to,
        "type" -> "route",
        "route" -> routeTagValue,
        "network:type" -> "node_network",
        "network" -> scopedNetworkType.key
      ) ++ routeTags
    )
    val rawData = RawData(None, nodeBuffer.toSeq, wayBuffer.toSeq, Seq(relation))
    new StructureTestSetup(new DataBuilder(rawData).data)
  }
}
