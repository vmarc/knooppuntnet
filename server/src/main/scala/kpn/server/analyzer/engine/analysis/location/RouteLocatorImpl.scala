package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.location.LocationCandidate
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisSegment
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.springframework.stereotype.Component

@Component
class RouteLocatorImpl(locationAnalyzer: LocationAnalyzer) extends RouteLocator {

  private val geometryFactory = new GeometryFactory

  def locate(segments: Seq[RouteAnalysisSegment]): RouteLocationAnalysis = {

    val geometries = toGeometries(segments)
    val candidates = locationAnalyzer.locateGeometries(geometries)

    val locationSelectorCandidates = candidates.map { candidate =>
      val distance = calculateDistance(geometries, candidate)
      LocationSelectorCandidate(candidate, distance)
    }

    if (locationSelectorCandidates.isEmpty) {
      RouteLocationAnalysis(None, Seq.empty, Seq.empty)
    }
    else {
      val sorted = locationSelectorCandidates.sortBy(_.distance).reverse

      val totalDistance = locationSelectorCandidates.map(_.distance).sum

      val locationCandidates = sorted.map { locationSelectorCandidate =>
        val percentage = Math.round(100d * locationSelectorCandidate.distance / totalDistance).toInt
        LocationCandidate(locationSelectorCandidate.selector.toLocation, percentage)
      }

      val locationNames = locationCandidates.flatMap(_.location.names).sorted.distinct

      RouteLocationAnalysis(
        Some(sorted.head.selector.toLocation),
        locationCandidates,
        locationNames
      )
    }
  }

  private def toGeometries(segments: Seq[RouteAnalysisSegment]): Seq[Geometry] = {
    segments.flatMap(_.elements).flatMap(_.fragmentGroups).map(_.lineString)
  }

  private def lineLength(geometry: Geometry): Double = {
    geometry match {
      case lineString: LineString => Haversine.meters(lineString)
      case multiLineString: MultiLineString =>
        0.until(multiLineString.getNumGeometries).map { index =>
          Haversine.meters(multiLineString.getGeometryN(index).asInstanceOf[LineString])
        }.sum

      case _ => 0d
    }
  }

  private def calculateDistance(routeGeometries: Seq[Geometry], location: LocationSelector): Double = {
    routeGeometries.map(_.intersection(location.leaf.geometry.geometry)).map(lineLength).sum
  }
}
