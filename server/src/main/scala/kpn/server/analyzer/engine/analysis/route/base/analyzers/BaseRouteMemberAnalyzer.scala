package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.data.Member
import kpn.api.common.data.MemberType
import kpn.api.common.data.Way
import kpn.api.common.route.LinkDirection
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.core.analysis.TagInterpreter
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.OneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteLinkWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.segment.SurfaceAnalyzer

import scala.collection.mutable

object BaseRouteMemberAnalyzer extends BaseRouteAnalyzer {
  private val log = Log(classOf[BaseRouteMemberAnalyzer])

  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteMemberAnalyzer(context).analyze
  }
}

class BaseRouteMemberAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
    val routeMembers = analyzeRouteMembers()
    context.copy(_routeMembers = Some(routeMembers))
  }

  private def analyzeRouteMembers(): Seq[RouteMemberInfo] = {
    // map with key Node.id and value node number
    val nodeMap: scala.collection.mutable.Map[Long, Int] = scala.collection.mutable.Map.empty
    val nodeNumberIterator = (1 to 10000).iterator
    val validRouteMembers: Seq[Member] = filterValidRouteMembers()
    val links = context.links.routeLinkWays
    val linkIterator = links.iterator
    validRouteMembers.flatMap { member =>
      if (member.isNode) {
        Some(processNodeMember(nodeMap, nodeNumberIterator, member))
      }
      else if (member.isWay) {
        Some(processWayMember(nodeMap, nodeNumberIterator, linkIterator, member))
      }
      else if (member.isRelationId) {
        Some(processRelationIdMember(member))
      }
      else {
        None
      }
    }
  }

  private def processNodeMember(nodeMap: mutable.Map[Long, Int], nodeNumberIterator: Iterator[Int], nodeMember: Member): RouteMemberInfo = {
    val node = nodeMember.node.get

    val name = context.routeNodeInfos.get(node.id).map(_.name).getOrElse("")
    val longName = context.routeNodeInfos.get(node.id).flatMap(_.longName)

    val number = getOrAssignNodeNumber(node.id, nodeMap, nodeNumberIterator)

    val alternateName = context.routeNodesAnalysis.nodes.find(rn => rn.node.id == node.id)
      .map(_.alternateName)
      .getOrElse(name)

    val memberName = node.tagValue("name")
    val poi = RouteMemberPoiAnalyzer.analyze(nodeMember)

    RouteMemberInfo(
      node.id,
      MemberType.Node,
      nodeMember.role,
      memberName,
      poi,
      None,
      Seq.empty,
      Seq.empty
    )
  }

  private def processWayMember(
    nodeMap: mutable.Map[Long, Int],
    nodeNumberIterator: Iterator[Int],
    linkIterator: Iterator[RouteLinkWay],
    wayMember: Member
  ): RouteMemberInfo = {

    val link = linkIterator.next()
    val way = wayMember.way.get
    val wayNetworkNodes = getWayNetworkNodes(way)

    val name = way.tagValue("name").getOrElse("")

    val fromNode = findFromNode(link, way)
    val toNode = findToNode(link, way)

    val from = getOrAssignNodeNumber(fromNode.id, nodeMap, nodeNumberIterator)
    val to = getOrAssignNodeNumber(toNode.id, nodeMap, nodeNumberIterator)

    val accessible = context.routeTypes.exists { routeType =>
      AccessibilityAnalyzer.accessible(
        wayMember.role,
        routeType,
        way
      )
    }

    val ferry = way.hasTag("route", "ferry")

    val routeNetworkNodeInfos = buildRouteNetworkNodeInfos(wayNetworkNodes)
    val wayType = new RouteWayTypeAnalyzer().analyze(wayMember)
    val memberName = way.tagValue("name")

    val poi = wayType match {
      case None => RouteMemberPoiAnalyzer.analyze(wayMember)
      case _ => None
    }

    val fromNodeName = getNodeName(fromNode.id)
    val toNodeName = getNodeName(toNode.id)

    val surface = new SurfaceAnalyzer(context.routeTypes, way).surface()

    val memberIndex = link.link.memberIndex
    val segmentElementIds = context.segmentElements.filter(_.memberIndexes.contains(memberIndex)).map(_.segmentElementId)
    val segmentIds = context.segments.filter(_.elementIds.exists(segmentElementIds.contains)).map(_.id)
    val pathIds = context.paths.filter(_.elementIds.exists(segmentElementIds.contains)).map(_.id)

    RouteMemberInfo(
      way.id,
      MemberType.Way,
      wayMember.role,
      memberName,
      poi,
      Some(
        RouteMemberInfoWay(
          wayType = wayType,
          nodes = routeNetworkNodeInfos,
          timestamp = way.timestamp,
          accessible = accessible,
          ferry = ferry,
          surface = surface,
          distance = way.length,
          nodeCount = way.nodes.size.toString,
          oneWay = new OneWayAnalyzer(way).direction,
          oneWayTags = OneWayAnalyzer.oneWayTags(way),
          link = link.link
        )
      ),
      segmentIds,
      pathIds
    )
  }

  private def buildRouteNetworkNodeInfos(wayNetworkNodes: Vector[RouteNodeAnalysis]) = {
    wayNetworkNodes.map(_.toRouteNode).map { rn =>
      RouteNetworkNodeInfo(
        rn.nodeId,
        rn.name,
        rn.alternateName,
        rn.longName,
        rn.latitude,
        rn.longitude
      )
    }
  }

  private def processRelationIdMember(relationIdMember: Member): RouteMemberInfo = {
    RouteMemberInfo(
      relationIdMember.relationId.get,
      MemberType.Relation,
      relationIdMember.role,
      None,
      None,
      None,
      Seq.empty,
      Seq.empty
    )
  }

  private def filterValidRouteMembers(): Seq[Member] = {
    context.relation.members.filter { member =>
      if (context.nodeNetwork) {
        TagInterpreter.isValidNetworkMember(context.scopedRouteType, member)
      }
      else {
        true
      }
    }
  }

  private def getOrAssignNodeNumber(
    nodeId: Long,
    nodeMap: mutable.Map[Long, Int],
    nodeNumberIterator: Iterator[Int]
  ): Int = {
    if (nodeMap.isDefinedAt(nodeId)) {
      nodeMap(nodeId)
    } else {
      val n = nodeNumberIterator.next()
      nodeMap(nodeId) = n
      n
    }
  }

  private def getWayNetworkNodes(way: Way): Vector[RouteNodeAnalysis] = {
    way.nodes.filter { n =>
      if (context.nodeNetwork) {
        TagInterpreter.isReferencedNetworkNode(context.scopedRouteType, n)
      } else {
        false
      }
    }.flatMap(n => context.routeNodesAnalysis.nodes.find(_.node.id == n.id))
  }

  private def findFromNode(link: RouteLinkWay, way: Way) = {
    if (link.link.direction == LinkDirection.Forward) way.nodes.head else way.nodes.last
  }

  private def findToNode(link: RouteLinkWay, way: Way) = {
    if (link.link.direction == LinkDirection.Backward) way.nodes.last else way.nodes.head
  }

  private def getNodeName(nodeId: Long): String = {
    context.routeNodesAnalysis.nodes.find(_.node.id == nodeId).map(_.name).getOrElse("")
  }
}

