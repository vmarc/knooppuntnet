package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.api.custom.Relation

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
}
