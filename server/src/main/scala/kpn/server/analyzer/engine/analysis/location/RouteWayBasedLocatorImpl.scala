package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LocationCandidate
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.TrackSegment
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteInfoAnalysis
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString

//@Component
class RouteWayBasedLocatorImpl(configuration: LocationConfiguration) extends RouteWayBasedLocator {

  private val geomFactory = new GeometryFactory

  def locate(route: RouteInfo): Option[RouteLocationAnalysis] = {

    val routeGeometries: Seq[Geometry] = toGeometries(route)

    val candidates: Seq[LocationSelector] = configuration.locations.flatMap { location =>
      doLocate(routeGeometries, Seq(), location)
    }.toVector

    val locationSelectorCandidates = candidates.map { candidate =>
      val distance = calculateDistance(routeGeometries, candidate)
      LocationSelectorCandidate(candidate, distance)
    }

    if (locationSelectorCandidates.isEmpty) {
      None
    }
    else {
      val sorted = locationSelectorCandidates.sortBy(_.distance).reverse

      val totalDistance = locationSelectorCandidates.map(_.distance).sum

      val ccc = if (sorted.size > 1) {
        sorted.map { locationSelectorCandidate =>
          val percentage = Math.round(100d * locationSelectorCandidate.distance / totalDistance).toInt
          LocationCandidate(locationSelectorCandidate.selector.toLocation, percentage)
        }
      }
      else {
        Seq.empty
      }
      Some(RouteLocationAnalysis(sorted.head.selector.toLocation, ccc))
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
        toSegments(analysis).map { segment: TrackSegment =>
          val coordinates = (segment.source +: segment.fragments.map(_.trackPoint)).map { trackPoint =>
            val lat = trackPoint.lat.toDouble
            val lon = trackPoint.lon.toDouble
            new Coordinate(lon, lat)
          }
          geomFactory.createLineString(coordinates.toArray)
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
