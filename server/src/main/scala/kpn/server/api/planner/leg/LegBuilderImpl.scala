package kpn.server.api.planner.leg

import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.RouteLeg
import kpn.api.common.planner.RouteLegFragment
import kpn.api.common.planner.RouteLegNode
import kpn.api.common.planner.RouteLegRoute
import kpn.api.common.planner.RouteLegSegment
import kpn.api.common.route.RouteInfo
import kpn.api.custom.NetworkType
import kpn.core.planner.graph.GraphPath
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.util.Log
import kpn.server.repository.GraphRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

case class Alternative(sourceNodeId: Long, sinkNodeId: Long)

@Component
class LegBuilderImpl(
  graphRepository: GraphRepository,
  routeRepository: RouteRepository
) extends LegBuilder {

  private val log = Log(classOf[LegBuilderImpl])

  override def build(params: LegBuildParams): Option[RouteLeg] = {
    NetworkType.withName(params.networkType) match {
      case Some(networkType) => build(params, networkType)
      case None =>
        log.error("Unknown network type " + params.networkType)
        None
    }
  }

  private def build(params: LegBuildParams, networkType: NetworkType): Option[RouteLeg] = {
    graphRepository.graph(networkType) match {
      case Some(graph) => buildLeg(params, graph)
      case None =>
        log.error("Could not find graph for network type " + networkType.name)
        None
    }
  }

  private def buildLeg(params: LegBuildParams, graph: NodeNetworkGraph): Option[RouteLeg] = {

    val routeIds = params.routeIds
    val routeInfos = routeIds.flatMap { routeId =>
      routeRepository.routeWithId(routeId) match {
        case Some(routeInfo) => Some(routeInfo.id -> routeInfo)
        case None =>
          log.error(s"via-route ${routeId} not found")
          None
      }
    }.toMap

    if (routeIds.size != routeInfos.size) {
      log.error(s"building leg aborted")
      None
    }
    else {
      val sourceNodeIds = legEndNodes(routeInfos, params.source)
      val sinkNodeIds = legEndNodes(routeInfos, params.sink).filterNot(sourceNodeIds.contains)
      val alternatives = for (x <- sourceNodeIds; y <- sinkNodeIds) yield Alternative(x, y)
      val routeLegAlternatives: Seq[RouteLeg] = alternatives.flatMap { alternative =>
        graph.findPath(alternative.sourceNodeId, alternative.sinkNodeId) match {
          case Some(graphPath) => Some(RouteLeg(params.legId, graphPathToRouteLegRoutes(graphPath)))
          case None => None
        }
      }

      if (routeLegAlternatives.nonEmpty) {
        val allNodeIds = (sourceNodeIds ++ sinkNodeIds).toSet
        val alts = routeLegAlternatives.filter(leg => (allNodeIds -- leg.allNodeIds).isEmpty)
        Some(alts.minBy(routeLeg => routeLeg.meters))
      }
      else {
        None
      }
    }
  }

  private def legEndNodes(routeInfos: Map[Long, RouteInfo], legEnd: LegEnd): Seq[Long] = {
    legEnd.node.toSeq.map(_.nodeId) ++
      legEnd.route.toSeq.flatMap(legEndRoute => {
        routeInfos.get(legEndRoute.routeId) match {
          case None => Seq() // Internal error ?
          case Some(routeInfo) =>
            routeInfo.analysis.map.paths.find(_.pathId == legEndRoute.pathId) match {
              case Some(trackPath) =>
                Seq(
                  trackPath.startNodeId,
                  trackPath.endNodeId
                )
              case None =>
                log.error(s"via-route ${legEndRoute.routeId} path ${legEndRoute.pathId} not found")
                Seq()
            }
        }
      })
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
