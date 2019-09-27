package kpn.core.engine.analysis.location

import kpn.shared.LocationCandidate
import kpn.shared.RouteLocationAnalysis
import kpn.shared.route.RouteInfo
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString

class RouteWayBasedLocatorImpl(configuration: LocationConfiguration) extends RouteWayBasedLocator {

  private val geomFactory = new GeometryFactory

  def locate(route: RouteInfo): Option[RouteLocationAnalysis] = {

    toGeometry(route).map { routeGeometry =>
      // TODO add bounding box checks?
      val candidates: Seq[LocationSelector] = configuration.locations.flatMap { location =>
        doLocate(routeGeometry, Seq(), location)
      }.toVector

      val locationSelectorCandidates = candidates.map { candidate =>
        val distance = lineLength(routeGeometry.intersection(candidate.leaf.geometry))
        LocationSelectorCandidate(candidate, distance)
      }

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

      RouteLocationAnalysis(sorted.head.selector.toLocation, ccc)
    }
  }

  private def doLocate(routeGeometry: Geometry, foundLocations: Seq[LocationDefinition], location: LocationDefinition): Seq[LocationSelector] = {

    if (location.geometry.intersects(routeGeometry)) {
      val newfoundLocations = foundLocations :+ location

      if (location.children.isEmpty) {
        Seq(LocationSelector(newfoundLocations))
      }
      else {
        location.children.flatMap { child =>
          doLocate(routeGeometry, newfoundLocations, child)
        }
      }
    }
    else {
      Seq()
    }
  }

  private def toGeometry(route: RouteInfo): Option[Geometry] = {
    route.analysis match {
      case None => None
      case Some(analysis) =>
        analysis.map.forwardPath match {
          case None => None
          case Some(path) =>
            val coordinates = path.segments.flatMap { segment =>
              (segment.source +: segment.fragments.map(_.trackPoint)).map { trackPoint =>
                val lat = trackPoint.lat.toDouble
                val lon = trackPoint.lon.toDouble
                new Coordinate(lon, lat)
              }
            }
            Some(geomFactory.createLineString(coordinates.toArray))
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

}
