package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.Member
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Fact.RouteInaccessible
import kpn.core.analysis.LinkDirection
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberNode
import kpn.core.analysis.RouteMemberWay
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

object RouteMemberAnalyzer extends RouteAnalyzer {

  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteMemberAnalyzer(context).analyze
  }
}

class RouteMemberAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    val routeMembers: Seq[RouteMember] = analyzeRouteMembers(context.routeNodesAnalysis)
    if (routeMembers.exists(!_.accessible)) {
      context.copy(_routeMembers = Some(routeMembers)).withFact(RouteInaccessible)
    }
    else {
      context.copy(_routeMembers = Some(routeMembers))
    }
  }

  private def analyzeRouteMembers(nodes: RouteNodesAnalysis): Seq[RouteMember] = {
    // map with key Node.id and value node number
    val nodeMap: scala.collection.mutable.Map[Long, Int] = scala.collection.mutable.Map.empty
    val nodeNumberIterator = (1 to 10000).iterator
    val validRouteMembers: Seq[Member] = context.relation.members.filter { member =>
      if (context.nodeNetwork) {
        TagInterpreter.isValidNetworkMember(context.scopedNetworkType, member)
      }
      else {
        true
      }
    }

    val links = context.links.links.flatMap {
      case routeWayLink: RouteLinkWay => Some(routeWayLink)
      case _ => None
    }

    //links.zip(relationMembers).toSeq.map { case(link, w) => LinkInfo(link, w)}

    val linkIterator = links.iterator
    //    val wayMemberIterator = validRouteMembers.filter(_.isWay).iterator
    //    val nodeMemberIterator = validRouteMembers.filter(_.isNode).iterator
    validRouteMembers.flatMap {
      case nodeMember: NodeMember =>

        val node = nodeMember.node

        val name = context.routeNodeInfos.get(node.id).map(_.name).getOrElse("")
        val longName = context.routeNodeInfos.get(node.id).flatMap(_.longName)

        val number = if (nodeMap.isDefinedAt(node.id)) {
          nodeMap(node.id)
        }
        else {
          val n = nodeNumberIterator.next()
          nodeMap(node.id) = n
          n
        }

        val alternateName = nodes.nodes.find(rn => rn.node.id == node.id).map(_.alternateName) match {
          case Some(aname) => aname
          case _ => name
        }

        Some(
          RouteMemberNode(
            name,
            alternateName,
            longName,
            number.toString,
            nodeMember.role,
            node
          )
        )

      case wayMember: WayMember =>

        // relationMember.isWay)
        val link = linkIterator.next()
        val way = wayMember.way
        val wayNetworkNodes = way.nodes.filter { n =>
          if (context.nodeNetwork) {
            TagInterpreter.isReferencedNetworkNode(context.scopedNetworkType, n)
          }
          else {
            false
          }
        }.flatMap(n => nodes.nodes.find(_.node.id == n.id))
        val name = way.tagValue("name").getOrElse("")

        val fromNode = if (link.link.direction == LinkDirection.Forward) way.nodes.head else way.nodes.last
        val toNode = if (link.link.direction == LinkDirection.Backward) way.nodes.last else way.nodes.head

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

        val accessible = new AccessibilityAnalyzerImpl().accessible(context.networkTypes.head /*TODO redesign - support multiple networkTypes*/ , way)

        // way.tags.has("route", "ferry") TODO draw boat icon?

        // some ways have <tag k="route" v="bicycle"/>; Is this enough to decide that this is ok ???

        Some(
          RouteMemberWay(
            name,
            Some(link.link),
            wayMember.role,
            way,
            fromNode,
            toNode,
            from.toString,
            to.toString,
            accessible,
            wayNetworkNodes.map(_.toRouteNode)
          )
        )

      case relationIdMember: RelationIdMember =>
        // TODO redesign - process relationIdMember
        None

      case relationMember: RelationMember =>
        // TODO redesign - process relationMember
        None
    }
  }
}
