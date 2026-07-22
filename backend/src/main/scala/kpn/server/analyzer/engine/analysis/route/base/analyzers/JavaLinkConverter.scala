package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Relation
import kpn.api.common.data.Member
import kpn.api.common.route.Link
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
    val links = relation.members.flatMap {
      case m if m.isNode => Some(toRouteLinkNode(m))
      case m if m.isWay => Some(toRouteLinkWay(javaWayLinks, linkIds, m))
      case m if m.isRelationId => Some(toRouteLinkRelationId(m))
      case _ => None
    }
    RouteLinks(links)
  }

  private def toRouteLinkRelationId(relationIdMember: Member): RouteLinkRelationId = {
    RouteLinkRelationId(
      relationIdMember.role,
      relationIdMember.relationId.get
    )
  }

  private def toRouteLinkWay(javaWayLinks: Iterator[JavaLink], linkIds: Iterator[Long], wayMember: Member): RouteLinkWay = {
    RouteLinkWay(
      linkIds.next(),
      toScalaLink(javaWayLinks.next()),
      wayMember.role,
      wayMember.way.get
    )
  }

  private def toRouteLinkNode(nodeMember: Member): RouteLinkNode = {
    RouteLinkNode(
      nodeMember.role,
      nodeMember.node.get
    )
  }

  private def toScalaLink(link: JavaLink): Link = {
    Link(
      link.memberIndex,
      JavaDirectionConverter.toScalaDirection(link.direction),
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
