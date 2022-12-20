package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.WayAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.FragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.MonitorSegmentBuilder
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport.toMeters
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteOsmSegmentAnalysis
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteSegmentData
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

class MonitorRouteOsmSegmentAnalyzer() {

  private val geomFactory = new GeometryFactory
  private val log = Log(classOf[MonitorRouteOsmSegmentAnalyzer])

  def analyze(wayMembers: Seq[WayMember]): MonitorRouteOsmSegmentAnalysis = {

    val fragmentMap = new FragmentAnalyzer(Seq.empty, wayMembers).fragmentMap

    val segments = log.infoElapsed {
      ("segment builder", new MonitorSegmentBuilder(NetworkType.hiking, fragmentMap, pavedUnpavedSplittingEnabled = false).segments(fragmentMap.ids))
    }

    val filteredSegments = segments.filterNot { segment =>
      segment.fragments.forall(segmentFragment => WayAnalyzer.isRoundabout(segmentFragment.fragment.way))
    }.filterNot(_.nodes.size == 1) // TODO investigate why segment with one node in route P-GR128


    val routeSegments = filteredSegments.zipWithIndex.map { case (segment, index) =>

      val lineString = geomFactory.createLineString(segment.nodes.map(node => new Coordinate(node.lon, node.lat)).toArray)
      val meters: Long = Math.round(toMeters(lineString.getLength))
      val bounds = MonitorRouteAnalysisSupport.toBounds(lineString.getCoordinates.toSeq)
      val geoJson = MonitorRouteAnalysisSupport.toGeoJson(lineString)

      val startId = {
        segment.fragments.headOption match {
          case None =>
            0 // TODO throw exception
          case Some(f) =>
            f.fragment.nodes.headOption match {
              case None =>
                0 // TODO throw exception
              case Some(n) => n.id
            }
        }
      }
      val endId = {
        segment.fragments.lastOption match {
          case None =>
            0 // TODO throw exception
          case Some(f) =>
            f.fragment.nodes.lastOption match {
              case None =>
                0 // TODO throw exception
              case Some(n) => n.id
            }
        }
      }

      MonitorRouteSegmentData(
        index + 1,
        MonitorRouteSegment(
          index + 1,
          startId,
          endId,
          meters,
          bounds,
          geoJson
        ),
        lineString
      )
    }

    val osmDistance = Math.round(routeSegments.map(_.segment.meters).sum.toDouble / 1000)

    MonitorRouteOsmSegmentAnalysis(
      osmDistance,
      routeSegments
    )
  }
}
