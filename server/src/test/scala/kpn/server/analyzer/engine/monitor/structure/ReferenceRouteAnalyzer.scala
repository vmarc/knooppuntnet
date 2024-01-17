package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.monitor.structure.reference.WayInfo

import java.util.Collections
import java.util.stream.Collectors.toUnmodifiableList
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava

class ReferenceRouteAnalyzer {

  def analyze(relation: Relation): Seq[String] = {
    val referenceRelation = buildReferenceRelation(relation)
    val calculator = new kpn.server.analyzer.engine.monitor.structure.reference.WayInfoCalculator(referenceRelation, referenceRelation.getMembers())
    calculator.calculate().asScala.toSeq.zipWithIndex.map { case (wayInfo, index) => s"${index + 1} ${wayInfoString(wayInfo)}" }
  }

  private def buildReferenceRelation(relation: Relation): kpn.server.analyzer.engine.monitor.structure.reference.Relation = {
    val nodeMap = mutable.Map[Long, kpn.server.analyzer.engine.monitor.structure.reference.Node]()
    val referenceRelationMembers = relation.members.map { member =>
      member match {
        case wayMember: WayMember =>
          val referenceRole = buildReferenceRole(wayMember)
          val referenceNodes = buildReferenceNodes(nodeMap, wayMember)
          val referenceTags = buildReferenceTags(wayMember)
          val referenceWay = new kpn.server.analyzer.engine.monitor.structure.reference.Way(
            wayMember.way.id,
            referenceTags,
            referenceNodes
          )
          new kpn.server.analyzer.engine.monitor.structure.reference.Member(referenceRole, referenceWay)

        case _ => throw new IllegalStateException("non way member types not implemented yet")
      }
    }.asJavaCollection.stream.collect(toUnmodifiableList())
    new kpn.server.analyzer.engine.monitor.structure.reference.Relation(referenceRelationMembers)
  }

  private def buildReferenceRole(member: Member): String = {
    member.role match {
      case None => ""
      case Some(role) => role
    }
  }

  private def buildReferenceNodes(nodeMap: mutable.Map[Long, kpn.server.analyzer.engine.monitor.structure.reference.Node], wayMember: WayMember): java.util.List[kpn.server.analyzer.engine.monitor.structure.reference.Node] = {
    wayMember.way.nodes.map { node =>
      nodeMap.getOrElseUpdate(node.id, new kpn.server.analyzer.engine.monitor.structure.reference.Node(node.id))
    }.asJavaCollection.stream.collect(toUnmodifiableList())
  }

  private def buildReferenceTags(wayMember: WayMember): java.util.Map[String, String] = {
    val tags = new java.util.HashMap[String, String]()
    wayMember.way.tags.tags.foreach { tag =>
      tags.put(tag.key, tag.value)
    }
    Collections.unmodifiableMap(tags)
  }

  private def wayInfoString(wayInfo: WayInfo): String = {
    val sb = new StringBuilder
    sb.append("   p " + bool(wayInfo.linkedToPreviousMember))
    sb.append("   n " + bool(wayInfo.linkedToNextMember))
    sb.append("   loop " + bool(wayInfo.isLoop))
    sb.append("   fp " + bool(wayInfo.isOnewayLoopForwardPart))
    sb.append("   bp " + bool(wayInfo.isOnewayLoopBackwardPart))
    sb.append("   head " + bool(wayInfo.isOnewayHead))
    sb.append("   tail " + bool(wayInfo.isOnewayTail))
    sb.append(String.format("   d %s", wayInfo.direction.toString.toLowerCase))
    sb.toString
  }

  private def bool(value: Boolean): String = {
    if (value) "■" else " "
  }
}
