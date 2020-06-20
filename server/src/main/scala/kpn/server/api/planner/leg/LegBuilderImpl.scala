package kpn.server.api.planner.leg

import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.RouteLeg
import kpn.api.common.planner.RouteLegFragment
import kpn.api.common.planner.RouteLegNode
import kpn.api.common.planner.RouteLegRoute
import kpn.api.common.planner.RouteLegSegment
import kpn.api.common.route.RouteInfo
import kpn.core.planner.graph.GraphPath
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.util.Log
import kpn.server.repository.GraphRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class LegBuilderImpl(
  graphRepository: GraphRepository,
  routeRepository: RouteRepository
) extends LegBuilder {

  private val log = Log(classOf[LegBuilderImpl])

  override def build(params: LegBuildParams): Option[RouteLeg] = {
    graphRepository.graph(params.networkType) match {
      case Some(graph) => buildLeg(params, graph)
      case None =>
        log.error("Could not find graph for network type " + params.networkType.name)
        None
    }
  }

  private def buildLeg(params: LegBuildParams, graph: NodeNetworkGraph): Option[RouteLeg] = {
    params.viaRoute match {
      case Some(viaRoute) => buildLegViaRoute(params, graph, viaRoute)
      case None => buildLegBetweenNodes(params, graph)
    }
  }

  private def buildLegViaRoute(params: LegBuildParams, graph: NodeNetworkGraph, viaRoute: ViaRoute): Option[RouteLeg] = {
    routeRepository.routeWithId(viaRoute.routeId) match {
      case Some(routeInfo) => buildLegViaRoute(params, graph, viaRoute, routeInfo)
      case None =>
        log.error(s"via-route ${viaRoute.routeId} not found")
        None
    }
  }

  private def buildLegViaRoute(params: LegBuildParams, graph: NodeNetworkGraph, viaRoute: ViaRoute, routeInfo: RouteInfo): Option[RouteLeg] = {
    routeInfo.analysis.map.paths.find(_.pathId == viaRoute.pathId) match {
      case Some(trackPath) => buildLegRoute(params, graph, routeInfo, trackPath)
      case None =>
        log.error(s"via-route ${viaRoute.routeId} path ${viaRoute.pathId} not found")
        None
    }
  }

  private def buildLegRoute(params: LegBuildParams, graph: NodeNetworkGraph, routeInfo: RouteInfo, trackPath: TrackPath): Option[RouteLeg] = {

    val routeLeg1 = routeLegAlternative(params, graph, routeInfo, trackPath, trackPath.startNodeId, trackPath.endNodeId)
    val routeLeg2 = if (!trackPath.oneWay) {
      routeLegAlternative(params, graph, routeInfo, trackPath, trackPath.endNodeId, trackPath.startNodeId)
    }
    else {
      None
    }
    val alternatives = Seq(routeLeg1, routeLeg2).flatten

    if (alternatives.nonEmpty) {
      Some(alternatives.minBy(routeLeg => routeLeg.meters))
    }
    else {
      None
    }
  }

  private def routeLegAlternative(params: LegBuildParams, graph: NodeNetworkGraph, routeInfo: RouteInfo, trackPath: TrackPath, nodeId1: Long, nodeId2: Long): Option[RouteLeg] = {
    graph.findPath(params.sourceNodeId, nodeId1) match {
      case None => None
      case Some(path1) =>
        graph.findPath(nodeId2, params.sinkNodeId) match {
          case Some(path2) => yyy(params, routeInfo, trackPath, path1, path2)
          case None => None
        }
    }
  }

  private def yyy(params: LegBuildParams, routeInfo: RouteInfo, trackPath: TrackPath, path1: GraphPath, path2: GraphPath): Option[RouteLeg] = {
    val colour = routeInfo.tags("colour")
    trackPathToRouteLegRoute(routeInfo, trackPath, colour) match {
      case None => None // TODO add error log message
      case Some(connection) =>
        val routes = graphPathToRouteLegRoutes(path1) ++ Seq(connection) ++ graphPathToRouteLegRoutes(path2)
        Some(RouteLeg(params.legId, routes))
    }
  }

  private def buildLegBetweenNodes(params: LegBuildParams, graph: NodeNetworkGraph): Option[RouteLeg] = {
    graph.findPath(params.sourceNodeId, params.sinkNodeId) match {
      case Some(graphPath) => Some(RouteLeg(params.legId, graphPathToRouteLegRoutes(graphPath)))
      case None =>
        log.error(s"Could not find ${params.networkType.name} path between ${params.sourceNodeId} and ${params.sinkNodeId}")
        None
    }
  }

  private def graphPathToRouteLegRoutes(graphPath: GraphPath): Seq[RouteLegRoute] = {
    graphPath.segments.flatMap { graphPathSegment =>
      val routeId = graphPathSegment.pathKey.routeId
      routeRepository.routeWithId(routeId) match {
        case Some(route) =>
          val colour = route.tags("colour")
          route.analysis.map.paths.find(_.pathId == graphPathSegment.pathKey.pathId) match {
            case Some(trackPath) => trackPathToRouteLegRoute(route, trackPath, colour)
            case None => None
          }

        case None =>
          log.error(s"route $routeId not found")
          None
      }
    }
  }

  private def trackPathToRouteLegRoute(routeInfo: RouteInfo, trackPath: TrackPath, colour: Option[String]): Option[RouteLegRoute] = {

    val routeLegSegments = trackPath.segments.map(s => toRouteLegSegment(s, colour))

    routeInfo.analysis.map.nodeWithId(trackPath.endNodeId) match {
      case Some(sinkRouteNetworkNodeInfo) =>
        val sinkNodeId = sinkRouteNetworkNodeInfo.id.toString
        val sinkodeName = sinkRouteNetworkNodeInfo.name
        val sink = RouteLegNode(sinkNodeId, sinkodeName, sinkRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)

        routeInfo.analysis.map.nodeWithId(trackPath.startNodeId) match {
          case Some(sourceRouteNetworkNodeInfo) =>
            val sourceNodeId = sourceRouteNetworkNodeInfo.id.toString
            val sourceNodeName = sourceRouteNetworkNodeInfo.name
            val source = RouteLegNode(sourceNodeId, sourceNodeName, sourceRouteNetworkNodeInfo.lat, sourceRouteNetworkNodeInfo.lon)
            val meters = routeLegSegments.map(_.meters).sum
            Some(RouteLegRoute(source, sink, meters, routeLegSegments, routeInfo.analysis.map.streets))

          case None =>
            log.error(s"route ${routeInfo.id} source node ${trackPath.startNodeId} not found")
            None
        }

      case None =>
        log.error(s"route ${routeInfo.id} sink node ${trackPath.endNodeId} not found")
        None
    }
  }

  private def toRouteLegSegment(trackSegment: TrackSegment, colour: Option[String]): RouteLegSegment = {
    val routeLegFragments = trackSegment.fragments.map(toRouteLegFragment)
    val meters = routeLegFragments.map(_.meters).sum
    RouteLegSegment(
      meters,
      trackSegment.surface,
      colour,
      routeLegFragments
    )
  }

  private def toRouteLegFragment(trackSegmentFragment: TrackSegmentFragment): RouteLegFragment = {
    RouteLegFragment(
      trackSegmentFragment.trackPoint.lat,
      trackSegmentFragment.trackPoint.lon,
      trackSegmentFragment.meters,
      trackSegmentFragment.orientation,
      trackSegmentFragment.streetIndex
    )
  }
}
