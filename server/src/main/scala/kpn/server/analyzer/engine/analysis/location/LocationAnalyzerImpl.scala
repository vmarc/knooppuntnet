package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.core.util.Log
import org.locationtech.jts.geom.Geometry
import org.springframework.stereotype.Component

@Component
class LocationAnalyzerImpl(analyzerEnabled: Boolean) extends LocationAnalyzerAbstract {

  private val log = Log(classOf[LocationAnalyzerImpl])

  private val locationStore = if (analyzerEnabled) {
    new LocationStoreReader().read()
  }
  else {
    LocationStore(Seq.empty)
  }

  override def countries(latLon: LatLon): Seq[Country] = {
    locationStore.countries.filter { countryLocations =>
      countryLocations.dataMap.get(countryLocations.country.domain) match {
        case Some(loc) => loc.contains(latLon.latitude, latLon.longitude)
        case None => // throw exception ? log error?
          false
      }
    }.map(_.country)
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    log.debugElapsed {
      ("Country analyzed", doCountry(latLons))
    }
  }

  override def findLocations(latitude: String, longitude: String): Seq[String] = {
    locationStore.countries.foreach { country =>
      val locationNames = doLocate(latitude, longitude, Seq.empty, country.tree)
      if (locationNames.nonEmpty) {
        return locationNames
      }
    }
    Seq.empty
  }

  override def locateGeometries(geometries: Seq[Geometry]): Seq[LocationSelector] = {
    locationStore.countries.flatMap { country =>
      doLocateGeometries(geometries, Seq.empty, country.tree)
    }.toVector
  }

  private def doLocateGeometries(routeGeometries: Seq[Geometry], foundLocations: Seq[LocationStoreData], location: LocationStoreData): Seq[LocationSelector] = {
    if (routeIntersectsLocation(routeGeometries, location)) {
      val newfoundLocations = foundLocations :+ location
      if (location.children.isEmpty) {
        Seq(LocationSelector(newfoundLocations))
      }
      else {
        location.children.flatMap { child =>
          doLocateGeometries(routeGeometries, newfoundLocations, child)
        }
      }
    }
    else {
      Seq.empty
    }
  }

  private def routeIntersectsLocation(routeGeometries: Seq[Geometry], location: LocationStoreData): Boolean = {
    routeGeometries.exists(location.geometry.geometry.intersects)
  }

  private def doLocate(latitude: String, longitude: String, names: Seq[String], locator: LocationStoreData): Seq[String] = {

    if (locator.contains(latitude, longitude)) {
      val newNames = names :+ locator.id

      if (locator.children.isEmpty) {
        newNames
      }
      else {
        locator.children.foreach { child =>
          val locationNames = doLocate(latitude, longitude, newNames, child)
          if (locationNames.nonEmpty) {
            return locationNames
          }
        }
        Seq.empty
      }
    }
    else {
      Seq.empty
    }
  }
}
