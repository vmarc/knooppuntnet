package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.monitor.reference.WayInfo

import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava

class ReferenceRouteAnalyzer {

  def analyze(relation: Relation): Seq[String] = {
    val referenceRelation = buildReferenceRelation(relation)
    val calculator = new reference.WayInfoCalculator(referenceRelation, referenceRelation.getMembers())
    calculator.calculate().asScala.toSeq.zipWithIndex.map { case (wayInfo, index) => s"${index + 1} ${wayInfoString(wayInfo)}" }
  }

  private def buildReferenceRelation(relation: Relation): reference.Relation = {
    val nodeMap = mutable.Map[Long, reference.Node]()
    val referenceRelationMembers = relation.members.map { member =>
      member match {
        case wayMember: WayMember =>
          val referenceRole = wayMember.role match {
            case None => ""
            case Some(role) => role
          }
          val referenceNodes = wayMember.way.nodes.map { node =>
            nodeMap.getOrElseUpdate(node.id, new reference.Node(node.id))
          }.asJavaCollection
          val referenceWay = new reference.Way(wayMember.way.id, referenceNodes)
          new reference.Member(referenceRole, referenceWay)

        case _ => throw new IllegalStateException("non way member types not implemented yet")
      }
    }.asJavaCollection
    new reference.Relation(referenceRelationMembers)
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
