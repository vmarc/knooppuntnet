package kpn.server.analyzer.engine.analysis.country

import kpn.api.custom.Country
import kpn.api.custom.Relation
import kpn.api.common.LatLon

class CountryAnalyzerNoop extends CountryAnalyzer {
  override def countries(latLon: LatLon): Seq[Country] = Seq.empty
  override def country(latLons: Iterable[LatLon]): Option[Country] = None
  override def relationCountry(relation: Relation): Option[Country] = None
}
