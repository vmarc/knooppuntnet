package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.data.WayMember
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteSegment
import kpn.server.analyzer.engine.analysis.route.RouteSegmentAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteSegmentData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.ElementDirection
import kpn.server.analyzer.engine.analysis.route.structure.StructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.structure.StructureElementAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

object RouteSegmentAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val wayMembers = context.relation.wayMembers
    val segmentAnalysis = new RouteSegmentAnalyzer().analyze(wayMembers)
    context.copy(
      segmentAnalysis = Some(segmentAnalysis)
    )
  }

  def analyze(wayMembers: Seq[WayMember]): RouteSegmentAnalysis = {
    new RouteSegmentAnalyzer().analyze(wayMembers)
  }
}

class RouteSegmentAnalyzer {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[RouteSegmentAnalyzer])

  def analyze(wayMembers: Seq[WayMember]): RouteSegmentAnalysis = {

    val elementGroups = try {
      StructureElementAnalyzer.analyze(wayMembers)
    }
    catch {
      case e: Exception =>
        log.error("Could not analyze structure", e)
        Seq.empty
    }

    val structure = new StructureAnalyzer().analyze(elementGroups)

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

        val segment = RouteSegment(
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
      routeSegments,
      structure
    )
  }
}
