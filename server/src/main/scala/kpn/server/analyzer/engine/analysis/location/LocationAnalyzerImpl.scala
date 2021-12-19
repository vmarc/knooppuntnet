package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.LatLon
import kpn.api.custom.Country
import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class LocationAnalyzerImpl(analyzerEnabled: Boolean) extends LocationAnalyzerAbstract {

  private val log = Log(classOf[LocationAnalyzerImpl])

  private val locationStore = if (analyzerEnabled) {
    new LocationStoreReader().read()
  }
  else {
    LocationStore(Seq.empty)
  }

  override def countries(latLon: LatLon): Seq[Country] = {
    locationStore.countries.filter { countryLocations =>
      countryLocations.dataMap.get(countryLocations.country.domain) match {
        case Some(loc) => loc.contains(latLon.latitude, latLon.longitude)
        case None => // throw exception ? log error?
          false
      }
    }.map(_.country)
  }

  override def country(latLons: Iterable[LatLon]): Option[Country] = {
    log.debugElapsed {
      ("Country analyzed", doCountry(latLons))
    }
  }
}
