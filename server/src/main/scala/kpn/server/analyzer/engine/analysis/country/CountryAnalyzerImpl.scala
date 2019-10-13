package kpn.server.analyzer.engine.analysis.country

import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer
import kpn.core.util.Log
import kpn.shared.Country
import kpn.shared.LatLon
import org.springframework.stereotype.Component

@Component
class CountryAnalyzerImpl(relationAnalyzer: RelationAnalyzer) extends CountryAnalyzerAbstract(relationAnalyzer) {

  private val log = Log(classOf[CountryAnalyzerImpl])

  private val countryBoundaries = Country.all.map { country =>
    country -> CountryBoundaryReader.read(country)
  }.toMap

  override def countries(latLon: LatLon): Seq[Country] = {
    countryBoundaries.filter { case (country, boundary) =>
      boundary.contains(latLon.latitude, latLon.longitude)
    }.keys.toSeq
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    log.debugElapsed {
      ("Country analyzed", doCountry(latLons))
    }
  }
}
