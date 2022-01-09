package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.NL
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest

class LocationServiceTest extends UnitTest {

  test("location key translation with key matching the language parameter") {
    translate("Duitsland:Sleeswijk-Holstein") should equal("de-1-01")
  }

  test("location key translation with key NOT matching the language parameter") {
    translate("Deutschland:Schleswig-Holstein") should equal("de-1-01")
  }

  test("mixed location name") {
    translate("Germany:Schleswig-Holstein") should equal("de-1-01")
  }

  private def translate(locationName: String): String = {
    val locationConfiguration = LocationConfigurationTest.locationConfiguration
    val locationService = new LocationServiceImpl(locationConfiguration)
    val key = LocationKey(NetworkType.cycling, Country.de, locationName)
    val translatedKey = locationService.translate(NL, key)
    translatedKey.name
  }
}
