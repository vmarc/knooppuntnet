package kpn.server.analyzer.engine.analysis.country

import kpn.api.common.LatLon
import kpn.api.custom.Country

class CountryAnalyzerMock extends CountryAnalyzerAbstract {

  override def countries(latLon: LatLon): Seq[Country] = {
    if (latLon.latitude == "" || latLon.latitude == "0") {
      Seq(Country.nl)
    }
    else {
      Seq.empty
    }
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    doCountry(latLons)
  }
}
