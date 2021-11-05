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
import kpn.api.custom.NetworkType
import kpn.core.doc.RouteDoc
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

  override def plan(networkType: NetworkType, planString: String, encoded: Boolean, proposed: Boolean): Option[Seq[PlanLegDetail]] = {
    graphRepository.graph(networkType) match {
      case Some(graph) =>
        val legEnds = LegEnd.fromPlanString(planString, encoded)
        val planLegDetails = legEndsToPlanLegs(networkType, graph, legEnds, Seq.empty, proposed)
        if (planLegDetails.nonEmpty) {
          Some(planLegDetails)
        }
        else {
          None
        }

      case None =>
        log.error("Could not find graph for network type " + networkType.name)
        None
    }
  }

  @scala.annotation.tailrec
  private def legEndsToPlanLegs(
    networkType: NetworkType,
    graph: NodeNetworkGraph,
    legEnds: Seq[LegEnd],
    legs: Seq[PlanLegDetail],
    proposed: Boolean
  ): Seq[PlanLegDetail] = {
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
          sink,
          proposed
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToPlanLegs(networkType, graph, legEnds.tail.tail, legs :+ routeLeg, proposed)
          case None => Seq.empty
        }
      }
      else {
        val source = LegEnd.node(legs.last.routes.last.sinkNode.nodeId.toLong)
        val sink = legEnds.head
        val params = LegBuildParams(
          networkType.name,
          source,
          sink,
          proposed
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToPlanLegs(networkType, graph, legEnds.tail, legs :+ routeLeg, proposed)
          case None => Seq.empty
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
    val routeDocs = routeIds.flatMap { routeId =>
      routeRepository.findById(routeId) match {
        case Some(routeDoc) => Some(routeDoc.id -> routeDoc)
        case None =>
          log.error(s"via-route $routeId not found")
          None
      }
    }.toMap

    if (routeIds.size != routeDocs.size) {
      log.error(s"building leg aborted")
      None
    }
    else {
      val sources = params.source.vertices
      val sinks = params.sink.vertices

      val alternatives = for (source <- sources; sink <- sinks) yield {
        graph.findPath(source, sink, params.proposed) match {
          case Some(graphPath) =>
            val planRoutes = graphPathToPlanRoutes(graphPath)
            val sourceLegEnd = LegEnd.fromString(source)

            val sinkLegEndSelection = LegEnd.fromString(sink)

            val sinkLegEnd: LegEnd = sinkLegEndSelection.route match {
              case Some(legEndRoute) =>
                val allTrackPathKeys = params.sink.route.get.trackPathKeys
                val selectedTrackPathKey = legEndRoute.trackPathKeys.head
                LegEnd.route(allTrackPathKeys, Some(selectedTrackPathKey))
              case _ => sinkLegEndSelection
            }

            Some(
              PlanLegDetail(
                sourceLegEnd,
                sinkLegEnd,
                planRoutes
              )
            )
          case None => None
        }
      }

      if (alternatives.flatten.nonEmpty) {
        Some(alternatives.flatten.minBy(_.meters))
      }
      else {
        None
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

    val segments = combineIdenticalPathSegments(graphPath.segments, Seq.empty)

    segments.flatMap { graphPathSegment =>
      val routeId = graphPathSegment.pathKey.routeId
      routeRepository.findById(routeId) match {
        case Some(route) =>
          val pathId = if (graphPathSegment.pathKey.pathId < 100) graphPathSegment.pathKey.pathId else graphPathSegment.pathKey.pathId - 100
          val colour = route.tags("colour")
          route.analysis.map.paths.find(_.pathId == pathId) match {
            case None => None
            case Some(trackPath) =>
              trackPathToPlanRoute(route, trackPath, colour).map { planRoute =>
                if (graphPathSegment.pathKey.pathId > 100) {
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

  private def trackPathToPlanRoute(routeDoc: RouteDoc, trackPath: TrackPath, colour: Option[String]): Option[PlanRoute] = {

    val routeLegSegments = trackPath.segments.map(s => toPlanSegment(s, colour))

    routeDoc.analysis.map.nodeWithId(trackPath.startNodeId) match {
      case Some(sourceRouteNetworkNodeInfo) =>
        val sourceNodeId = sourceRouteNetworkNodeInfo.id.toString
        val sourceNodeName = sourceRouteNetworkNodeInfo.name
        val sourceNodeLongName = sourceRouteNetworkNodeInfo.longName

        val sourceCoordinate = PlanUtil.toCoordinate(sourceRouteNetworkNodeInfo.lat.toDouble, sourceRouteNetworkNodeInfo.lon.toDouble)
        val sourceLatLon = LatLonImpl(sourceRouteNetworkNodeInfo.lat, sourceRouteNetworkNodeInfo.lon)

        val sourceNode = PlanNode(
          featureId.next,
          sourceNodeId,
          sourceNodeName,
          sourceNodeLongName,
          sourceCoordinate,
          sourceLatLon
        )

        routeDoc.analysis.map.nodeWithId(trackPath.endNodeId) match {

          case Some(sinkRouteNetworkNodeInfo) =>
            val sinkNodeId = sinkRouteNetworkNodeInfo.id.toString
            val sinkNodeName = sinkRouteNetworkNodeInfo.name
            val sinkNodeLongName = sinkRouteNetworkNodeInfo.longName
            val sinkCoordinate = PlanUtil.toCoordinate(sinkRouteNetworkNodeInfo.lat.toDouble, sinkRouteNetworkNodeInfo.lon.toDouble)
            val sinkLatLon = LatLonImpl(sinkRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)

            val sinkNode = PlanNode(
              featureId.next,
              sinkNodeId,
              sinkNodeName,
              sinkNodeLongName,
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
                routeDoc.analysis.map.streets
              )
            )

          case None =>
            log.error(s"route ${routeDoc.id} source node ${trackPath.startNodeId} not found")
            None
        }

      case None =>
        log.error(s"route ${routeDoc.id} sink node ${trackPath.endNodeId} not found")
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
