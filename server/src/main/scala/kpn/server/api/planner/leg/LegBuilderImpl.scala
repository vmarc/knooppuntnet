package kpn.server.api.planner.leg

import kpn.api.common.LatLonImpl
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.PlanFragment
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.planner.PlanNode
import kpn.api.common.planner.PlanRoute
import kpn.api.common.planner.PlanSegment
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
  private val featureId = new FeatureId()

  override def leg(params: LegBuildParams): Option[PlanLegDetail] = {
    NetworkType.withName(params.networkType) match {
      case Some(networkType) => buildLeg(params, networkType)
      case None =>
        log.error("Unknown network type " + params.networkType)
        None
    }
  }

  override def plan(networkType: NetworkType, planString: String, encoded: Boolean): Option[PlanLegDetail] = {
    graphRepository.graph(networkType) match {
      case Some(graph) =>
        val planLegDetails = loadPlanLegs(networkType, graph, planString, encoded)
        if (planLegDetails.isEmpty) {
          None
        }
        else {
          val allRoutes = planLegDetails.flatMap(_.routes)
          Some(PlanLegDetail(allRoutes))
        }

      case None =>
        log.error("Could not find graph for network type " + networkType.name)
        None
    }
  }

  private def loadPlanLegs(networkType: NetworkType, graph: NodeNetworkGraph, planString: String, encoded: Boolean): Seq[PlanLegDetail] = {
    val legEnds = LegEnd.fromPlanString(planString, encoded)
    legEndsToPlanLegs(networkType, graph, legEnds, Seq())
  }

  @scala.annotation.tailrec
  private def legEndsToPlanLegs(networkType: NetworkType, graph: NodeNetworkGraph, legEnds: Seq[LegEnd], legs: Seq[PlanLegDetail]): Seq[PlanLegDetail] = {
    if (legEnds.isEmpty) {
      legs
    }
    else {
      if (legs.isEmpty) {
        val source = legEnds.head
        val sink = legEnds.tail.head
        val params = LegBuildParams(
          networkType.name,
          source,
          sink
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToPlanLegs(networkType, graph, legEnds.tail.tail, legs :+ routeLeg)
          case None => Seq()
        }
      }
      else {
        val source = LegEnd.node(legs.last.routes.last.sinkNode.nodeId.toLong)
        val sink = legEnds.head
        val params = LegBuildParams(
          networkType.name,
          source,
          sink
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToPlanLegs(networkType, graph, legEnds.tail, legs :+ routeLeg)
          case None => Seq()
        }
      }
    }
  }

  private def buildLeg(params: LegBuildParams, networkType: NetworkType): Option[PlanLegDetail] = {
    graphRepository.graph(networkType) match {
      case Some(graph) => buildLeg(params, graph)
      case None =>
        log.error("Could not find graph for network type " + networkType.name)
        None
    }
  }

  private def buildLeg(params: LegBuildParams, graph: NodeNetworkGraph): Option[PlanLegDetail] = {

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
        case Some(graphPath) =>

          val planRoutes = graphPathToPlanRoutes(graphPath)

          Some(
            PlanLegDetail(
              planRoutes
            )
          )
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

  private def graphPathToPlanRoutes(graphPath: GraphPath): Seq[PlanRoute] = {

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
              trackPathToPlanRoute(route, trackPath, colour).map { planRoute =>
                if (graphPathSegment.pathKey.pathId < 0) {
                  planRoute.reverse
                }
                else {
                  planRoute
                }
              }
          }

        case None =>
          log.error(s"route $routeId not found")
          None
      }
    }
  }

  private def trackPathToPlanRoute(routeInfo: RouteInfo, trackPath: TrackPath, colour: Option[String]): Option[PlanRoute] = {

    val routeLegSegments = trackPath.segments.map(s => toPlanSegment(s, colour))

    routeInfo.analysis.map.nodeWithId(trackPath.startNodeId) match {
      case Some(sourceRouteNetworkNodeInfo) =>
        val sourceNodeId = sourceRouteNetworkNodeInfo.id.toString
        val sourceNodeName = sourceRouteNetworkNodeInfo.name

        val sourceCoordinate = PlanUtil.toCoordinate(sourceRouteNetworkNodeInfo.lat.toDouble, sourceRouteNetworkNodeInfo.lon.toDouble)
        val sourceLatLon = LatLonImpl(sourceRouteNetworkNodeInfo.lat, sourceRouteNetworkNodeInfo.lon)

        val sourceNode = PlanNode(
          featureId.next,
          sourceNodeId,
          sourceNodeName,
          sourceCoordinate,
          sourceLatLon
        )

        routeInfo.analysis.map.nodeWithId(trackPath.endNodeId) match {

          case Some(sinkRouteNetworkNodeInfo) =>
            val sinkNodeId = sinkRouteNetworkNodeInfo.id.toString
            val sinkNodeName = sinkRouteNetworkNodeInfo.name
            val sinkCoordinate = PlanUtil.toCoordinate(sinkRouteNetworkNodeInfo.lat.toDouble, sinkRouteNetworkNodeInfo.lon.toDouble)
            val sinkLatLon = LatLonImpl(sinkRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)

            val sinkNode = PlanNode(
              featureId.next,
              sinkNodeId,
              sinkNodeName,
              sinkCoordinate,
              sinkLatLon
            )

            val meters = routeLegSegments.map(_.meters).sum

            Some(
              PlanRoute(
                sourceNode,
                sinkNode,
                meters,
                routeLegSegments,
                routeInfo.analysis.map.streets
              )
            )

          case None =>
            log.error(s"route ${routeInfo.id} source node ${trackPath.startNodeId} not found")
            None
        }

      case None =>
        log.error(s"route ${routeInfo.id} sink node ${trackPath.endNodeId} not found")
        None
    }
  }

  private def toPlanSegment(trackSegment: TrackSegment, colour: Option[String]): PlanSegment = {
    val planFragments = trackSegment.fragments.map(toPlanFragment)
    val meters = planFragments.map(_.meters).sum
    PlanSegment(
      meters,
      trackSegment.surface,
      colour,
      planFragments
    )
  }

  private def toPlanFragment(trackSegmentFragment: TrackSegmentFragment): PlanFragment = {

    val coordinate = PlanUtil.toCoordinate(trackSegmentFragment.trackPoint.lat.toDouble, trackSegmentFragment.trackPoint.lon.toDouble)
    val latLon = LatLonImpl(trackSegmentFragment.trackPoint.lat, trackSegmentFragment.trackPoint.lon)

    PlanFragment(
      trackSegmentFragment.meters,
      trackSegmentFragment.orientation,
      trackSegmentFragment.streetIndex,
      coordinate,
      latLon
    )
  }
}
