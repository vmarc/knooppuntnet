package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.TrackSegment
import kpn.api.common.location.LocationCandidate
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteInfoAnalysis
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.springframework.stereotype.Component

@Component
class RouteLocatorImpl(locationConfiguration: LocationConfiguration) extends RouteLocator {

  private val geomFactory = new GeometryFactory

  def locate(route: RouteInfo): RouteLocationAnalysis = {

    val routeGeometries: Seq[Geometry] = toGeometries(route)

    val candidates: Seq[LocationSelector] = locationConfiguration.locations.flatMap { location =>
      doLocate(routeGeometries, Seq(), location)
    }.toVector

    val locationSelectorCandidates = candidates.map { candidate =>
      val distance = calculateDistance(routeGeometries, candidate)
      LocationSelectorCandidate(candidate, distance)
    }

    if (locationSelectorCandidates.isEmpty) {
      RouteLocationAnalysis()
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

  private def doLocate(routeGeometries: Seq[Geometry], foundLocations: Seq[LocationDefinition], location: LocationDefinition): Seq[LocationSelector] = {
    if (routeIntersectsLocation(routeGeometries, location)) {
      val newfoundLocations = foundLocations :+ location
      if (location.children.isEmpty) {
        Seq(LocationSelector(newfoundLocations))
      }
      else {
        location.children.flatMap { child =>
          doLocate(routeGeometries, newfoundLocations, child)
        }
      }
    }
    else {
      Seq()
    }
  }

  private def routeIntersectsLocation(routeGeometries: Seq[Geometry], location: LocationDefinition): Boolean = {
    routeGeometries.exists(location.geometry.intersects)
  }

  private def toGeometries(route: RouteInfo): Seq[Geometry] = {
    route.analysis match {
      case None => Seq()
      case Some(analysis) =>
        toSegments(analysis).flatMap { segment: TrackSegment =>
          val coordinates = (segment.source +: segment.fragments.map(_.trackPoint)).map { trackPoint =>
            val lat = trackPoint.lat.toDouble
            val lon = trackPoint.lon.toDouble
            new Coordinate(lon, lat)
          }
          if (coordinates.size > 1) {
            Some(geomFactory.createLineString(coordinates.toArray))
          }
          else {
            None
          }
        }
    }
  }

  private def lineLength(geometry: Geometry): Double = {
    geometry match {
      case lineString: LineString => lineString.getLength
      case multiLineString: MultiLineString => multiLineString.getLength
      case _ => 0d
    }
  }

  private def toSegments(analysis: RouteInfoAnalysis): Seq[TrackSegment] = {
    Seq(
      analysis.map.forwardPath.toSeq.flatMap(_.segments),
      analysis.map.backwardPath.toSeq.flatMap(_.segments),
      analysis.map.unusedSegments,
      analysis.map.startTentaclePaths.flatMap(_.segments),
      analysis.map.endTentaclePaths.flatMap(_.segments)
    ).flatten
  }

  private def calculateDistance(routeGeometries: Seq[Geometry], location: LocationSelector): Double = {
    routeGeometries.map(_.intersection(location.leaf.geometry)).map(lineLength).sum
  }

}
