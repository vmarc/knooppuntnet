package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import org.springframework.stereotype.Component

@Component
class PoiScopeAnalyzerImpl(countryAnalyzer: CountryAnalyzer) extends PoiScopeAnalyzer {
  override def inScope(latLon: LatLon): Boolean = {
    val countries = countryAnalyzer.countries(latLon)
    countries.contains(Country.nl) || countries.contains(Country.be)
  }
}
