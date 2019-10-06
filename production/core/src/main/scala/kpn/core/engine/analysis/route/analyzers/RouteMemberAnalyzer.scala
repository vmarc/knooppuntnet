package kpn.core.engine.analysis.route.analyzers

import kpn.core.analysis.LinkType
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberNode
import kpn.core.analysis.RouteMemberWay
import kpn.core.engine.analysis.route.RouteNodeAnalysis
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.core.obsolete.OldLinkBuilder
import kpn.shared.Fact.RouteUnaccessible
import kpn.shared.data.Member
import kpn.shared.data.NodeMember
import kpn.shared.data.WayMember

object RouteMemberAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteMemberAnalyzer(context).analyze
  }
}

class RouteMemberAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {
    val routeMembers = analyzeRouteMembers(context.routeNodeAnalysis.get)
    if (routeMembers.exists(!_.accessible)) {
      context.copy(routeMembers = Some(routeMembers)).withFact(RouteUnaccessible)
    }
    else {
      context.copy(routeMembers = Some(routeMembers))
    }
  }

  private def analyzeRouteMembers(routeNodeAnalysis: RouteNodeAnalysis): Seq[RouteMember] = {
    // map with key Node.id and value node number
    val nodeMap: scala.collection.mutable.Map[Long, Int] = scala.collection.mutable.Map()
    val nodeNumberIterator = (1 to 10000).iterator
    val validRouteMembers: Seq[Member] = context.loadedRoute.relation.members.filter { member =>
      context.analysisContext.isValidNetworkMember(context.loadedRoute.networkType, member)
    }

    val wayRelationMembers = validRouteMembers.flatMap {
      case member: WayMember => Some(kpn.core.josm.RelationMember(member.role.getOrElse(""), member.way))
      case _ => None
    }

    val oldLinks = new OldLinkBuilder(wayRelationMembers).links

    //links.zip(relationMembers).toSeq.map { case(link, w) => LinkInfo(link, w)}

    val linkIterator = oldLinks.iterator
    //    val wayMemberIterator = validRouteMembers.filter(_.isWay).iterator
    //    val nodeMemberIterator = validRouteMembers.filter(_.isNode).iterator
    validRouteMembers.map {
      case nodeMember: NodeMember =>

        val node = nodeMember.node

        val name = context.networkNodes.get(node.id) match {
          case Some(networkNode) => networkNode.name
          case None => ""
        }

        val number = if (nodeMap.isDefinedAt(node.id)) {
          nodeMap(node.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(node.id) = n
          n
        }

        val alternateName = routeNodeAnalysis.routeNodes.find(rn => rn.id == node.id).map(_.alternateName) match {
          case Some(aname) => aname
          case _ => name
        }

        RouteMemberNode(name, alternateName, number.toString, nodeMember.role, node)


      case wayMember: WayMember =>

        // relationMember.isWay)
        val link = linkIterator.next()
        val way = wayMember.way
        val wayNetworkNodes = way.nodes.filter(n => context.analysisContext.isNetworkNode(context.networkType, n.raw)).flatMap(n => routeNodeAnalysis.routeNodes.find(_
          .id == n.id))
        val name = way.tags("name").getOrElse("")

        val fromNode = if (link.linkType == LinkType.FORWARD) way.nodes.head else way.nodes.last
        val toNode = if (link.linkType == LinkType.FORWARD) way.nodes.last else way.nodes.head

        val from = if (nodeMap.isDefinedAt(fromNode.id)) {
          nodeMap(fromNode.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(fromNode.id) = n
          n
        }

        val to = if (nodeMap.isDefinedAt(toNode.id)) {
          nodeMap(toNode.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(toNode.id) = n
          n
        }

        val accessible = new AccessibilityAnalyzerImpl().accessible(context.networkType, way)

        // way.tags.has("route", "ferry") TODO draw boat icon?

        // some ways have <tag k="route" v="bicycle"/>; Is this enough to decide that this is ok ???

        RouteMemberWay(name, link, wayMember.role, way, fromNode, toNode, from.toString, to.toString, accessible, wayNetworkNodes)
    }
  }

}
