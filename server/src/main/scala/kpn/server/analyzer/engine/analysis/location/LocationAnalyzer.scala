package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.api.custom.Relation
import org.locationtech.jts.geom.Geometry

trait LocationAnalyzer {

  def countries(latLon: LatLon): Seq[Country]

  def country(latLons: Iterable[LatLon]): Option[Country]

  def relationCountry(relation: Relation): Option[Country]

  def findLocations(latitude: String, longitude: String): Seq[String]

  def locateGeometries(geometries: Seq[Geometry]): Seq[LocationSelector]
}
