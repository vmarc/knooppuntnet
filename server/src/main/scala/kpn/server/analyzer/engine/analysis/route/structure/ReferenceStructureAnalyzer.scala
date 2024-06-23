package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import java.util.Collections
import java.util.stream.Collectors.toUnmodifiableList
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava

object ReferenceStructureAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val referenceStructure = new ReferenceStructureAnalyzer().analyze(context.relation)
    context.copy(
      referenceStructure = Some(referenceStructure)
    )
  }
}

class ReferenceStructureAnalyzer(traceEnabled: Boolean = false) {

  def analyze(relation: Relation): ReferenceStructure = {
    val referenceRelation = toJavaRelation(relation)
    val analyzer = new reference.WayInfoAnalyzer(referenceRelation, referenceRelation.getMembers(), traceEnabled)
    val javaWayInfos = analyzer.analyze().asScala.toSeq
    val wayInfos = javaWayInfos.map(toScalaWayInfo)
    ReferenceStructure(wayInfos)
  }

  private def toJavaRelation(relation: Relation): reference.Relation = {
    val nodeMap = mutable.Map[Long, reference.Node]()
    val referenceRelationMembers = relation.members.filter(_.isWay /*TODO redesign allow all member types */).map {
      case wayMember: WayMember =>
        val referenceRole = toJavaRole(wayMember)
        val referenceNodes = toJavaNodes(nodeMap, wayMember)
        val referenceTags = toJavaTags(wayMember)
        val referenceWay = new reference.Way(
          wayMember.way.id,
          referenceTags,
          referenceNodes
        )
        new reference.Member(referenceRole, referenceWay)

      //      case nodeMember: NodeMember =>
      //      case relationMember: RelationMember =>

      case _ => throw new IllegalStateException("non way member types not implemented yet")
    }.asJavaCollection.stream.collect(toUnmodifiableList())
    new reference.Relation(referenceRelationMembers)
  }

  private def toJavaRole(member: Member): String = {
    member.role match {
      case None => ""
      case Some(role) => role
    }
  }

  private def toJavaNodes(nodeMap: mutable.Map[Long, reference.Node], wayMember: WayMember): java.util.List[reference.Node] = {
    wayMember.way.nodes.map { node =>
      nodeMap.getOrElseUpdate(node.id, new reference.Node(node.id))
    }.asJavaCollection.stream.collect(toUnmodifiableList())
  }

  private def toJavaTags(wayMember: WayMember): java.util.Map[String, String] = {
    val tags = new java.util.HashMap[String, String]()
    wayMember.way.tags.foreach { tag =>
      tags.put(tag.key, tag.value)
    }
    Collections.unmodifiableMap(tags)
  }

  private def toScalaWayInfo(wayInfo: reference.WayInfo): ReferenceWayInfo = {
    ReferenceWayInfo(
      wayInfo.linkedToPreviousMember,
      wayInfo.linkedToNextMember,
      wayInfo.isLoop,
      wayInfo.isOnewayLoopForwardPart,
      wayInfo.isOnewayLoopBackwardPart,
      wayInfo.isOnewayHead,
      wayInfo.isOnewayTail,
      toScalaDirection(wayInfo.direction)
    )
  }

  private def toScalaDirection(direction: reference.WayInfo.Direction): ReferenceDirection = {
    if (direction == reference.WayInfo.Direction.FORWARD) {
      ReferenceDirection.Forward
    }
    else if (direction == reference.WayInfo.Direction.BACKWARD) {
      ReferenceDirection.Backward
    }
    else if (direction == reference.WayInfo.Direction.ROUNDABOUT_LEFT) {
      ReferenceDirection.RoundaboutLeft
    }
    else if (direction == reference.WayInfo.Direction.ROUNDABOUT_RIGHT) {
      ReferenceDirection.RoundaboutRight
    }
    else if (direction == reference.WayInfo.Direction.NONE) {
      ReferenceDirection.Unknown
    }
    else {
      throw new IllegalArgumentException(s"Unknown reference direction ${direction.toString}")
    }
  }
}
