package kpn.core.engine.analysis.country

import kpn.shared.data.Relation
import kpn.shared.Country
import kpn.shared.LatLon

trait CountryAnalyzer {
  def countries(latLon: LatLon): Seq[Country]
  def country(latLons: Iterable[LatLon]): Option[Country]
  def relationCountry(relation: Relation): Option[Country]
}
