package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.api.custom.Relation
import org.locationtech.jts.geom.Geometry

class LocationAnalyzerFixed extends LocationAnalyzer {

  override def countries(latLon: LatLon): Seq[Country] = {
    Seq(Country.nl)
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    Some(Country.nl)
  }

  override def relationCountry(relation: Relation): Option[Country] = {
    Some(Country.nl)
  }

  override def findLocations(latitude: String, longitude: String): Seq[String] = {
    Seq.empty
  }

  override def locateGeometries(geometries: Seq[Geometry]): Seq[LocationSelector] = {
    Seq.empty
  }
}
