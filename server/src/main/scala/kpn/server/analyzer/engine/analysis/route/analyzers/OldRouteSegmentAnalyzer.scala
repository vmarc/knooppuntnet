package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.WayMember
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.OldRouteSegment
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteSegmentAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.ElementDirection
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

object OldRouteSegmentAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val wayMembers = context.relation.wayMembers
    val segmentAnalysis = new OldRouteSegmentAnalyzer(context.nodeAnalysis).analyze(wayMembers)
    context.copy(
      _segmentAnalysis = Some(segmentAnalysis)
    )
  }
}

class OldRouteSegmentAnalyzer(routeNodeAnalysis: RouteNodeAnalysis) {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[OldRouteSegmentAnalyzer])

  def analyze(wayMembers: Seq[WayMember]): RouteSegmentAnalysis = {

    val elementGroups = try {
      StructureElementAnalyzer.analyze(routeNodeAnalysis, wayMembers)
    }
    catch {
      case e: Exception =>
        log.error("Could not analyze structure", e)
        Seq.empty
    }

    val nodeMap = {
      val nodes = wayMembers.flatMap(_.way.nodes).distinct
      nodes.map(node => node.id -> new Coordinate(node.lon, node.lat)).toMap
    }

    val routeSegments = elementGroups.zipWithIndex.flatMap { case (elementGroup, index) =>
      val lineStrings = elementGroup.elements.map { element =>
        val coordinates = element.nodeIds.flatMap(nodeMap.get)
        geometryFactory.createLineString(coordinates.toArray)
      }

      val forwardElements = elementGroup.elements.filter { element =>
        element.direction match {
          case Some(ElementDirection.Backward) => false
          case _ => true
        }
      }

      if (forwardElements.isEmpty) {
        // TODO should still look at backwardElements !!!
        None
      }
      else {

        val startNodeId = forwardElements.head.forwardStartNodeId
        val endNodeId = forwardElements.last.forwardEndNodeId

        val meters = Math.round(lineStrings.map(lineString => Haversine.meters(lineString)).sum)
        val allCoordinates = lineStrings.flatMap(lineString => lineString.getCoordinates.toSeq)
        val bounds = MonitorRouteAnalysisSupport.toBounds(allCoordinates)

        val geometryCollection = geometryFactory.createGeometryCollection(lineStrings.toArray)

        val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

        val segment = OldRouteSegment(
          id = index + 1,
          startNodeId,
          endNodeId,
          meters,
          bounds,
          geoJson
        )

        Some(
          RouteSegmentData(
            id = index + 1,
            segment,
            lineStrings
          )
        )
      }
    }
    val osmDistance = routeSegments.map(_.segment.meters).sum

    RouteSegmentAnalysis(
      osmDistance,
      elementGroups,
      routeSegments
    )
  }
}
