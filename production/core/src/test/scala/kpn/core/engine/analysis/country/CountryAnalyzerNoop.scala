package kpn.core.engine.analysis.country

import kpn.shared.data.Relation
import kpn.shared.Country
import kpn.shared.LatLon

class CountryAnalyzerNoop extends CountryAnalyzer {
  override def countries(latLon: LatLon): Seq[Country] = Seq.empty
  override def country(latLons: Iterable[LatLon]): Option[Country] = None
  override def relationCountry(relation: Relation): Option[Country] = None
}
