package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.NetworkType
import kpn.core.util.Haversine
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.WayAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.MonitorSegmentBuilder
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteOsmSegmentAnalysis
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class MonitorRouteOsmSegmentAnalyzerImpl() extends MonitorRouteOsmSegmentAnalyzer {

  private val geometryFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteOsmSegmentAnalyzerImpl])

  def analyze(wayMembers: Seq[WayMember]): MonitorRouteOsmSegmentAnalysis = {

    val fragmentMap = new FragmentAnalyzer(Seq.empty, wayMembers).fragmentMap

    val segments = log.infoElapsed {
      ("segment builder", new MonitorSegmentBuilder(NetworkType.hiking, fragmentMap, pavedUnpavedSplittingEnabled = false).segments(fragmentMap.ids))
    }

    val filteredSegments = segments.filterNot { segment =>
      segment.fragments.forall(segmentFragment => WayAnalyzer.isRoundabout(segmentFragment.fragment.way))
    }.filterNot(_.nodes.size == 1) // TODO investigate why segment with one node in route P-GR128

    val routeSegments = filteredSegments.zipWithIndex.map { case (segment, index) =>

      val lineString = geometryFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters = Math.round(Haversine.meters(lineString))
      val bounds = MonitorRouteAnalysisSupport.toBounds(lineString.getCoordinates.toSeq)
      val geoJson = MonitorRouteAnalysisSupport.toGeoJson(lineString)

      val startNodeId = segment.fragments.headOption.map(_.startNode.id).getOrElse(0L)
      val endNodeId = segment.fragments.lastOption.map(_.endNode.id).getOrElse(0L)

      MonitorRouteSegmentData(
        index + 1,
        MonitorRouteSegment(
          index + 1,
          startNodeId,
          endNodeId,
          meters,
          bounds,
          geoJson
        ),
        lineString
      )
    }

    val osmDistance = routeSegments.map(_.segment.meters).sum

    MonitorRouteOsmSegmentAnalysis(
      osmDistance,
      routeSegments
    )
  }
}
