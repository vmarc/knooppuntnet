package kpn.server.analyzer.engine.analysis.country

import kpn.api.custom.Country
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.api.common.LatLon

class CountryAnalyzerMock(relationAnalyzer: RelationAnalyzer) extends CountryAnalyzerAbstract(relationAnalyzer) {

  override def countries(latLon: LatLon): Seq[Country] = {
    if (latLon.latitude == "" || latLon.latitude == "0") {
      Seq(Country.nl)
    }
    else {
      Seq()
    }
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    doCountry(latLons)
  }
}
