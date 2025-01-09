package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteInaccessible
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.data.Member
import kpn.api.common.data.MemberType
import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationIdMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.common.route.LinkDirection
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.OneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis

object BaseRouteMemberAnalyzer extends BaseRouteAnalyzer {
  private val log = Log(classOf[BaseRouteMemberAnalyzer])

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteMemberAnalyzer(context).analyze
  }
}

class BaseRouteMemberAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val routeMembers: Seq[RouteMemberInfo] = analyzeRouteMembers(context.routeNodesAnalysis)
    val inaccessible = routeMembers.exists { member =>
      member.way match {
        case Some(wayInfo) => !wayInfo.accessible
        case None => false
      }
    }
    if (inaccessible) {
      context.copy(_routeMembers = Some(routeMembers)).withFact(RouteInaccessible)
    }
    else {
      context.copy(_routeMembers = Some(routeMembers))
    }
  }

  private def analyzeRouteMembers(nodes: RouteNodesAnalysis): Seq[RouteMemberInfo] = {
    // map with key Node.id and value node number
    val nodeMap: scala.collection.mutable.Map[Long, Int] = scala.collection.mutable.Map.empty
    val nodeNumberIterator = (1 to 10000).iterator
    val validRouteMembers: Seq[Member] = context.relation.members.filter { member =>
      if (context.nodeNetwork) {
        TagInterpreter.isValidNetworkMember(context.scopedRouteType, member)
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

        val nodesX: Seq[RouteNetworkNodeInfo] = Seq(
          RouteNetworkNodeInfo(node.id, name, alternateName, longName, node.latitude, node.longitude)
        )

        val memberName = nodeMember.node.tagValue("name")
        val poi = RouteMemberPoiAnalyzer.analyze(nodeMember)

        Some(
          RouteMemberInfo(
            node.id,
            MemberType.Node,
            nodeMember.role,
            memberName,
            poi,
            None,
          )
        )

      case wayMember: WayMember =>

        // relationMember.isWay)
        val link = linkIterator.next()
        val way = wayMember.way
        val wayNetworkNodes = way.nodes.filter { n =>
          if (context.nodeNetwork) {
            TagInterpreter.isReferencedNetworkNode(context.scopedRouteType, n)
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

        val accessible = new AccessibilityAnalyzerImpl().accessible(wayMember.role, context.routeTypes.head /*TODO redesign - support multiple routeTypes*/ , way)

        // way.tags.has("route", "ferry") TODO draw boat icon?

        // some ways have <tag k="route" v="bicycle"/>; Is this enough to decide that this is ok ???

        def nodesX: Seq[RouteNetworkNodeInfo] = wayNetworkNodes.map(_.toRouteNode).map { rn =>
          RouteNetworkNodeInfo(
            rn.nodeId,
            rn.name,
            rn.alternateName,
            None, // TODO redesign
            "TODO rn.latitude",
            "TODO rn.longitude"
          )
        }

        val wayType = new RouteWayTypeAnalyzer().analyze(wayMember)

        val memberName = wayMember.way.tagValue("name")

        val poi = wayType match {
          case None => RouteMemberPoiAnalyzer.analyze(wayMember)
          case _ => None
        }

        Some(
          RouteMemberInfo(
            way.id,
            MemberType.Way,
            wayMember.role,
            memberName,
            poi,
            Some(
              RouteMemberInfoWay(
                wayType = wayType,
                nodes = nodesX,
                from = fromNode.toString,
                fromNodeId = fromNode.id,
                to = toNode.toString,
                toNodeId = toNode.id,
                timestamp = way.timestamp,
                accessible = accessible,
                distance = way.length,
                nodeCount = way.nodes.size.toString,
                oneWay = new OneWayAnalyzer(way).direction,
                oneWayTags = OneWayAnalyzer.oneWayTags(way),
                link.link
              )
            )
          )
        )

      case relationIdMember: RelationIdMember =>

        Some(
          RouteMemberInfo(
            relationIdMember.relationId,
            MemberType.Relation,
            relationIdMember.role,
            None,
            None,
            None
          )
        )

      case relationMember: RelationMember =>
        // TODO redesign - process relationMember
        None
    }
  }
}
