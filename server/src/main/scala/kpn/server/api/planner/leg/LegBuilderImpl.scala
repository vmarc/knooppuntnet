package kpn.server.api.planner.leg

import kpn.api.common.LatLonImpl
import kpn.api.common.RouteType
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.PlanFragment
import kpn.api.common.planner.PlanLegDetail
import kpn.api.common.planner.PlanNode
import kpn.api.common.planner.PlanRoute
import kpn.api.common.planner.PlanSegment
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.BaseRoutePath
import kpn.core.planner.graph.GraphPath
import kpn.core.planner.graph.GraphPathSegment
import kpn.core.planner.graph.NodeNetworkGraph
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
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
    RouteType.withNameOption(params.routeType) match {
      case Some(routeType) => buildLeg(params, routeType)
      case None =>
        log.error(s"Unknown network type ${params.routeType}")
        None
    }
  }

  override def plan(routeType: RouteType, planString: String, encoded: Boolean, proposed: Boolean): Option[Seq[PlanLegDetail]] = {
    graphRepository.graph(routeType) match {
      case Some(graph) =>
        val legEnds = LegEnd.fromPlanString(planString, encoded)
        val planLegDetails = legEndsToPlanLegs(routeType, graph, legEnds, Seq.empty, proposed)
        if (planLegDetails.nonEmpty) {
          Some(planLegDetails)
        }
        else {
          None
        }

      case None =>
        log.error(s"Could not find graph for network type ${routeType.entryName}")
        None
    }
  }

  @scala.annotation.tailrec
  private def legEndsToPlanLegs(
    routeType: RouteType,
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
          routeType.entryName,
          source,
          sink,
          proposed
        )

        buildLeg(params, graph) match {
          case Some(routeLeg) => legEndsToPlanLegs(routeType, graph, legEnds.tail.tail, legs :+ routeLeg, proposed)
          case None => Seq.empty
        }
      }
      else {
        val source = LegEnd.node(legs.last.routes.last.sinkNode.nodeId.toLong)
        val sink = legEnds.head
        if (source == sink) {
          legEndsToPlanLegs(routeType, graph, legEnds.tail, legs, proposed)
        }
        else {
          val params = LegBuildParams(
            routeType.entryName,
            source,
            sink,
            proposed
          )
          buildLeg(params, graph) match {
            case Some(routeLeg) => legEndsToPlanLegs(routeType, graph, legEnds.tail, legs :+ routeLeg, proposed)
            case None => Seq.empty
          }
        }
      }
    }
  }

  private def buildLeg(params: LegBuildParams, routeType: RouteType): Option[PlanLegDetail] = {
    graphRepository.graph(routeType) match {
      case Some(graph) => buildLeg(params, graph)
      case None =>
        log.error(s"Could not find graph for network type ${routeType.entryName}")
        None
    }
  }

  private def buildLeg(params: LegBuildParams, graph: NodeNetworkGraph): Option[PlanLegDetail] = {

    val routeIds = params.routeIds
    val routeDocs = routeIds.flatMap { routeId =>
      routeRepository.findBaseRouteById(routeId) match {
        case Some(routeDoc) => Some(routeDoc.id -> routeDoc)
        case None =>
          log.error(s"via-route $routeId not found")
          None
      }
    }.toMap

    if (routeIds.sizeIs != routeDocs.size) {
      log.error("building leg aborted")
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
    else if (segments.sizeIs == 1) {
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
      routeRepository.findBaseRouteById(routeId) match {
        case Some(route) =>
          val pathId = if (graphPathSegment.pathKey.pathId < 100) {
            graphPathSegment.pathKey.pathId
          }
          else {
            graphPathSegment.pathKey.pathId - 100
          }
          val colour = route.summary.tagValue("colour")
          route.paths.find(_.id == pathId) match {
            case None => None
            case Some(baseRoutePath) =>
              trackPathToPlanRoute(route, baseRoutePath, colour).map { planRoute =>
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

  private def trackPathToPlanRoute(routeDoc: BaseRouteDoc, routePath: BaseRoutePath, colour: Option[String]): Option[PlanRoute] = {

    val startNodeId = {
      val startElementId = routePath.elementIds.head
      val startElement = routeDoc.segmentElements.find(_.segmentElementId == startElementId).get
      val startSegment = routeDoc.segments.find(_.id == startElement.segmentId).get
      startSegment.startNodeId
    }

    val endNodeId = {
      val endElementId = routePath.elementIds.last
      val endElement = routeDoc.segmentElements.find(_.segmentElementId == endElementId).get
      val endSegment = routeDoc.segments.find(_.id == endElement.segmentId).get
      endSegment.endNodeId
    }

    val routeLegSegments = routePath.elementIds.flatMap { elementId =>
      routeDoc.segmentElements.find(_.segmentElementId == elementId).map { segmentElement =>
        val coordinates = Json.value(segmentElement.coordinates, classOf[CoordinateArray]).coordinates.toSeq
        val planFragments = coordinates.sliding(2, 1).zipWithIndex.toSeq.flatMap { case (Seq(coordinate1, coordinate2), index) =>
          val meters = (Haversine.km(coordinate1.getX, coordinate1.getY, coordinate2.getX, coordinate2.getY) * 1000).toLong
          val latLon1 = LatLonImpl(coordinate1.getX.toString, coordinate1.getY.toString)
          val planCoordinate1 = PlanUtil.toCoordinate(coordinate1.getX, coordinate1.getY)
          val latLon2 = LatLonImpl(coordinate2.getX.toString, coordinate2.getY.toString)
          val planCoordinate2 = PlanUtil.toCoordinate(coordinate2.getX, coordinate2.getY)

          val fragment1 = PlanFragment(
            0,
            planCoordinate1,
            latLon1
          )
          val fragment2 = PlanFragment(
            meters,
            planCoordinate2,
            latLon2
          )

          if (index == 0) {
            Seq(fragment1, fragment2)
          }
          else {
            Seq(fragment2)
          }
        }

        val meters = planFragments.map(_.meters).sum

        PlanSegment(
          meters,
          segmentElement.surface,
          colour,
          planFragments
        )
      }
    }

    routeDoc.nodes.nodeWithId(startNodeId) match {
      case Some(sourceRouteNetworkNodeInfo) =>
        val sourceNodeId = sourceRouteNetworkNodeInfo.nodeId.toString
        val sourceNodeName = sourceRouteNetworkNodeInfo.name
        val sourceNodeLongName = Some("TODO CLEANUP")

        val sourceCoordinate = PlanUtil.toCoordinate(sourceRouteNetworkNodeInfo.lat, sourceRouteNetworkNodeInfo.lon)
        val sourceLatLon = LatLonImpl(sourceRouteNetworkNodeInfo.latitude, sourceRouteNetworkNodeInfo.longitude)

        val sourceNode = PlanNode(
          featureId.next,
          sourceNodeId,
          sourceNodeName,
          sourceNodeLongName,
          sourceCoordinate,
          sourceLatLon
        )

        routeDoc.nodes.nodeWithId(endNodeId) match {

          case Some(sinkRouteNetworkNodeInfo) =>
            val sinkNodeId = sinkRouteNetworkNodeInfo.nodeId.toString
            val sinkNodeName = sinkRouteNetworkNodeInfo.name
            val sinkNodeLongName = Some("TODO CLEANUP")
            val sinkCoordinate = PlanUtil.toCoordinate(sinkRouteNetworkNodeInfo.lat, sinkRouteNetworkNodeInfo.lon)
            val sinkLatLon = LatLonImpl(sinkRouteNetworkNodeInfo.latitude, sinkRouteNetworkNodeInfo.longitude)

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
                routeLegSegments
              )
            )

          case None =>
            log.error(s"route ${routeDoc.id} source node $startNodeId not found")
            None
        }

      case None =>
        log.error(s"route ${routeDoc.id} sink node $endNodeId not found")
        None
    }
  }
}
