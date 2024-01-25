package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder

import scala.collection.mutable.ListBuffer

class StructureTestSetup extends SharedTestObjects {

  private val nodeBuffer = ListBuffer[RawNode]()
  private val wayBuffer = ListBuffer[RawWay]()
  private val memberBuffer = ListBuffer[RawMember]()

  def memberWay(wayId: Long, role: String, nodeIds: Long*): RawMember = {
    memberWayWithTags(wayId, role, Tags.from("highway" -> "road"), nodeIds: _*)
  }

  def memberWayWithTags(wayId: Long, role: String, tags: Tags, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    memberWay(wayId, tags, role, nodeIds: _*)
  }

  private def node(id: Long, name: String = "", lattitude: Double = 0, longitude: Double = 0): RawNode = {
    rawNode(newRawNode(id, lattitude.toString, longitude.toString, tags = Tags.empty))
  }

  private def rawNode(rawNode: RawNode): RawNode = {
    nodeBuffer += rawNode
    rawNode
  }

  private def way(wayId: Long, nodeIds: Long*): RawWay = {
    way(wayId, Tags.empty, nodeIds: _*)
  }

  private def way(wayId: Long, tags: Tags, nodeIds: Long*): RawWay = {
    addNodesIfMissing(nodeIds)
    val w = newRawWay(wayId, nodeIds = nodeIds.toVector, tags = tags)
    wayBuffer += w
    w
  }

  private def memberWay(wayId: Long, tags: Tags, role: String, nodeIds: Long*): RawMember = {
    addNodesIfMissing(nodeIds)
    way(wayId, tags, nodeIds: _*)
    member("way", wayId, role)
  }

  private def member(memberType: String, ref: Long, role: String = ""): RawMember = {
    val m = RawMember(memberType, ref, if (role.nonEmpty) Some(role) else None)
    memberBuffer += m
    m
  }

  private def addNodesIfMissing(nodeIds: Seq[Long]): Unit = {
    val missingNodeIds = nodeIds.toSet -- nodeBuffer.map(_.id).toSet
    missingNodeIds.foreach(id => node(id))
  }

  def relation: Relation = {
    val relation = newRawRelation(1, members = memberBuffer.toSeq)
    val rawData = RawData(None, nodeBuffer.toSeq, wayBuffer.toSeq, Seq(relation))
    new DataBuilder(rawData).data.relations(1)
  }

  def elementGroups(traceEnabled: Boolean = false): Seq[Seq[String]] = {
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled)
    elementGroups.map(_.elements.map(_.string))
  }

  def reference(traceEnabled: Boolean = false): Seq[String] = {
    val reference = new ReferenceStructureAnalyzer(traceEnabled).analyze(relation)
    if (traceEnabled) println()
    if (traceEnabled) reference.foreach(println)
    if (traceEnabled) println()
    reference
  }

  def structure(traceEnabled: Boolean = false): TestStructure = {
    TestStructure.from(new StructureAnalyzer(traceEnabled).analyze(relation))
  }
}
