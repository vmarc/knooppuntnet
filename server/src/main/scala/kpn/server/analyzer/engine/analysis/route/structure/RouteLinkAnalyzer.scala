package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.core.analysis.Link
import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLink
import kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLinkAnalyzer

import java.util.Collections
import java.util.stream.Collectors.toUnmodifiableList
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava

object RouteLinkAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val links = new RouteLinkAnalyzer().analyze(context.relation)
    context.copy(
      _links = Some(links)
    )
  }
}

class RouteLinkAnalyzer(traceEnabled: Boolean = false) {

  def analyze(relation: Relation): RouteLinks = {
    val referenceRelation = toJavaRelation(relation)
    val analyzer = new ReferenceLinkAnalyzer(referenceRelation, referenceRelation.getMembers(), traceEnabled)
    val javaWayLinks = analyzer.analyze().asScala.iterator

    val linkIds = (1L to 10000L).iterator

    val links = relation.members.flatMap { member =>
      member match {
        case nodeMember: NodeMember =>
          Some(
            RouteLinkNode(
              nodeMember.role,
              nodeMember.node
            )
          )
        case wayMember: WayMember =>
          Some(
            RouteLinkWay(
              linkIds.next(),
              toScalaLink(javaWayLinks.next()),
              wayMember.role,
              wayMember.way,
              Seq.empty
            )
          )

        case relationIdMember: RelationIdMember =>
          Some(
            RouteLinkRelationId(
              relationIdMember.role,
              relationIdMember.relationId
            )
          )
        case _ => None
      }
    }

    RouteLinks(links)
  }

  private def toJavaRelation(relation: Relation): reference.Relation = {
    val nodeMap = mutable.Map[Long, reference.Node]()
    val referenceRelationMembers = relation.members.filter(_.isWay).map {
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

  private def toScalaLink(link: ReferenceLink): Link = {
    Link(
      toScalaDirection(link.direction),
      link.linkedToPreviousMember,
      link.linkedToNextMember,
      link.isLoop,
      link.isOnewayLoopForwardPart,
      link.isOnewayLoopBackwardPart,
      link.isOnewayHead,
      link.isOnewayTail
    )
  }

  private def toScalaDirection(direction: ReferenceLink.Direction): LinkDirection = {
    if (direction == reference.ReferenceLink.Direction.FORWARD) {
      LinkDirection.Forward
    }
    else if (direction == reference.ReferenceLink.Direction.BACKWARD) {
      LinkDirection.Backward
    }
    else if (direction == reference.ReferenceLink.Direction.ROUNDABOUT_LEFT) {
      LinkDirection.RoundaboutLeft
    }
    else if (direction == reference.ReferenceLink.Direction.ROUNDABOUT_RIGHT) {
      LinkDirection.RoundaboutRight
    }
    else if (direction == reference.ReferenceLink.Direction.NONE) {
      LinkDirection.Unconnected
    }
    else {
      throw new IllegalArgumentException(s"Unknown reference direction ${direction.toString}")
    }
  }
}
