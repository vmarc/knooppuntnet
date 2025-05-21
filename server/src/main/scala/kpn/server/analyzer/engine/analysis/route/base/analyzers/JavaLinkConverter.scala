package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.WayMember
import kpn.api.common.route.Link
import kpn.api.custom.Relation
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkNode
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkRelationId
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinks
import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaLink

import scala.jdk.CollectionConverters.CollectionHasAsScala

object JavaLinkConverter {

  def toScala(relation: Relation, javaLinks: java.util.List[JavaLink]): RouteLinks = {
    val javaWayLinks = javaLinks.asScala.iterator
    val linkIds = (1L to 10000L).iterator
    val links = relation.members.collect {
      case nodeMember: NodeMember => toRouteLinkNode(nodeMember)
      case wayMember: WayMember => toRouteLinkWay(javaWayLinks, linkIds, wayMember)
      case relationIdMember: RelationIdMember => toRouteLinkRelationId(relationIdMember)
    }
    RouteLinks(links)
  }

  private def toRouteLinkRelationId(relationIdMember: RelationIdMember): RouteLinkRelationId = {
    RouteLinkRelationId(
      relationIdMember.role,
      relationIdMember.relationId
    )
  }

  private def toRouteLinkWay(javaWayLinks: Iterator[JavaLink], linkIds: Iterator[Long], wayMember: WayMember): RouteLinkWay = {
    RouteLinkWay(
      linkIds.next(),
      toScalaLink(javaWayLinks.next()),
      wayMember.role,
      wayMember.way
    )
  }

  private def toRouteLinkNode(nodeMember: NodeMember): RouteLinkNode = {
    RouteLinkNode(
      nodeMember.role,
      nodeMember.node
    )
  }

  private def toScalaLink(link: JavaLink): Link = {
    Link(
      JavaDirectionConverter.toScala(link.direction),
      link.linkedToPreviousMember,
      link.linkedToNextMember,
      link.isLoop,
      link.isOnewayLoopForwardPart,
      link.isOnewayLoopBackwardPart,
      link.isOnewayHead,
      link.isOnewayTail
    )
  }
}
