package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.common.data.Member
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.core.analysis.Link
import kpn.core.analysis.LinkType
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLink
import kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLinkAnalyzer

import java.util.Collections
import java.util.stream.Collectors.toUnmodifiableList
import scala.collection.mutable
import scala.jdk.CollectionConverters.CollectionHasAsScala
import scala.jdk.CollectionConverters.IterableHasAsJava

object RouteLinkAnalyzer extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val referenceStructure = new RouteLinkAnalyzer().analyze(context.relation)
    context.copy(
      _referenceStructure = Some(referenceStructure)
    )
  }
}

class RouteLinkAnalyzer(traceEnabled: Boolean = false) {

  def analyze(relation: Relation): ReferenceStructure = {
    val referenceRelation = toJavaRelation(relation)
    val analyzer = new ReferenceLinkAnalyzer(referenceRelation, referenceRelation.getMembers(), traceEnabled)
    val javaReferenceLinks = analyzer.analyze().asScala.toSeq
    val links = javaReferenceLinks.map(toScalaLink)
    ReferenceStructure(links)
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

  private def toScalaDirection(direction: ReferenceLink.Direction): LinkType = {
    if (direction == reference.ReferenceLink.Direction.FORWARD) {
      LinkType.Forward
    }
    else if (direction == reference.ReferenceLink.Direction.BACKWARD) {
      LinkType.Backward
    }
    else if (direction == reference.ReferenceLink.Direction.ROUNDABOUT_LEFT) {
      LinkType.RoundaboutLeft
    }
    else if (direction == reference.ReferenceLink.Direction.ROUNDABOUT_RIGHT) {
      LinkType.RoundaboutRight
    }
    else if (direction == reference.ReferenceLink.Direction.NONE) {
      LinkType.All
    }
    else {
      throw new IllegalArgumentException(s"Unknown reference direction ${direction.toString}")
    }
  }
}
