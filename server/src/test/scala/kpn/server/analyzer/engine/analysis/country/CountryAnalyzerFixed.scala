package kpn.server.analyzer.engine.analysis.country

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.api.custom.Relation

class CountryAnalyzerFixed extends CountryAnalyzer {

  override def countries(latLon: LatLon): Seq[Country] = {
    Seq(Country.nl)
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    Some(Country.nl)
  }

  override def relationCountry(relation: Relation): Option[Country] = {
    Some(Country.nl)
  }
}
