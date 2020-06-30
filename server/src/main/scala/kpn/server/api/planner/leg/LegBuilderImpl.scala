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
import kpn.core.planner.graph.GraphPathSegment
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
    NetworkType.withName(params.networkType) match {
      case Some(networkType) => build(params, networkType)
      case None =>
        log.error("Unknown network type " + params.networkType)
        None
    }
  }

  override def load(networkType: NetworkType, planString: String, encoded: Boolean): Option[Seq[RouteLeg]] = {
    graphRepository.graph(networkType) match {
      case Some(graph) =>
        val legs = loadRouteLegs(networkType, graph, planString, encoded)
        if (legs.isEmpty) None else Some(legs)
      case None =>
        log.error("Could not find graph for network type " + networkType.name)
        None
    }
  }

  private def loadRouteLegs(networkType: NetworkType, graph: NodeNetworkGraph, planString: String, encoded: Boolean): Seq[RouteLeg] = {
    val legEnds = LegEnd.fromPlanString(planString, encoded)
    legEndsToRouteLegs(networkType, graph, 10001, legEnds, Seq())
  }

  @scala.annotation.tailrec
  private def legEndsToRouteLegs(networkType: NetworkType, graph: NodeNetworkGraph, legId: Long, legEnds: Seq[LegEnd], legs: Seq[RouteLeg]): Seq[RouteLeg] = {
    if (legEnds.isEmpty) {
      legs
    }
    else {
      if (legs.isEmpty) {
        val source = legEnds.head
        val sink = legEnds.tail.head
        val params = LegBuildParams(
          networkType.name,
          legId.toString,
          source,
          sink
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToRouteLegs(networkType, graph, legId + 1, legEnds.tail.tail, legs :+ routeLeg)
          case None => Seq()
        }
      }
      else {
        val source = LegEnd.node(legs.last.routes.last.sink.nodeId.toLong)
        val sink = legEnds.head
        val params = LegBuildParams(
          networkType.name,
          legId.toString,
          source,
          sink
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToRouteLegs(networkType, graph, legId + 1, legEnds.tail, legs :+ routeLeg)
          case None => Seq()
        }
      }
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
          log.error(s"via-route $routeId not found")
          None
      }
    }.toMap

    if (routeIds.size != routeInfos.size) {
      log.error(s"building leg aborted")
      None
    }
    else {
      val source = params.source.vertex
      val sink = params.sink.vertex
      graph.findPath(source, sink) match {
        case Some(graphPath) => Some(RouteLeg(params.legId, graphPathToRouteLegRoutes(graphPath)))
        case None => None
      }
    }
  }

  @scala.annotation.tailrec
  private def combineIdenticalPathSegments(segments: Seq[GraphPathSegment], result: Seq[GraphPathSegment]): Seq[GraphPathSegment] = {
    if (segments.isEmpty) {
      result
    }
    else if (segments.size == 1) {
      result :+ segments.head
    }
    else {
      val head = segments.head
      val next = segments.tail.head
      if (head.pathKey == next.pathKey) {
        combineIdenticalPathSegments(segments.tail.tail, result :+ GraphPathSegment(next.sink, next.pathKey))
      }
      else {
        combineIdenticalPathSegments(segments.tail, result :+ head)
      }
    }
  }

  private def graphPathToRouteLegRoutes(graphPath: GraphPath): Seq[RouteLegRoute] = {

    val segments = combineIdenticalPathSegments(graphPath.segments, Seq())

    segments.flatMap { graphPathSegment =>
      val routeId = graphPathSegment.pathKey.routeId
      routeRepository.routeWithId(routeId) match {
        case Some(route) =>
          val pathId = Math.abs(graphPathSegment.pathKey.pathId)
          val colour = route.tags("colour")
          route.analysis.map.paths.find(_.pathId == pathId) match {
            case None => None
            case Some(trackPath) =>
              trackPathToRouteLegRoute(route, trackPath, colour).map { routeLegRoute =>
                if (graphPathSegment.pathKey.pathId < 0) {
                  routeLegRoute.reverse
                }
                else {
                  routeLegRoute
                }
              }
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
