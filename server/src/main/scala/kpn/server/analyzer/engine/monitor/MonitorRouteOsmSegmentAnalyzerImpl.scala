package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteOsmSegmentAnalysis
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData
import kpn.server.analyzer.engine.monitor.structure.ElementDirection
import kpn.server.analyzer.engine.monitor.structure.StructureElementAnalyzer
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorRouteOsmSegmentAnalyzerImpl() extends MonitorRouteOsmSegmentAnalyzer {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteOsmSegmentAnalyzerImpl])

  def analyze(wayMembers: Seq[WayMember]): MonitorRouteOsmSegmentAnalysis = {

    val nodes = wayMembers.flatMap(_.way.nodes).distinct
    val nodeMap = nodes.map(node => node.id -> new Coordinate(node.lon, node.lat)).toMap

    val elementGroups = try {
      StructureElementAnalyzer.analyze(wayMembers)
    }
    catch {
      case e: Exception =>
        log.error("Could not analyze structure", e)
        Seq.empty
    }

    val routeSegments = elementGroups.zipWithIndex.map { case (elementGroup, index) =>
      val lineStrings = elementGroup.elements.map { element =>
        val coordinates = element.nodeIds.flatMap(nodeMap.get)
        geometryFactory.createLineString(coordinates.toArray)
      }

      val forwardElements = elementGroup.elements.filter { element =>
        element.direction match {
          case Some(ElementDirection.Up) => false
          case _ => true
        }
      }
      val startNodeId = forwardElements.head.forwardStartNodeId
      val endNodeId = forwardElements.last.forwardEndNodeId

      val meters = Math.round(lineStrings.map(lineString => Haversine.meters(lineString)).sum)
      val allCoordinates = lineStrings.flatMap(lineString => lineString.getCoordinates.toSeq)
      val bounds = MonitorRouteAnalysisSupport.toBounds(allCoordinates)

      val geometryCollection = geometryFactory.createGeometryCollection(lineStrings.toArray)

      val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometryCollection)

      val segment = MonitorRouteSegment(
        id = index + 1,
        startNodeId,
        endNodeId,
        meters,
        bounds,
        geoJson
      )

      MonitorRouteSegmentData(
        id = index + 1,
        segment,
        lineStrings
      )
    }
    val osmDistance = routeSegments.map(_.segment.meters).sum

    MonitorRouteOsmSegmentAnalysis(
      osmDistance,
      routeSegments
    )
  }
}
