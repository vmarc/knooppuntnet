package kpn.server.api.poi

import kpn.api.common.EN
import kpn.api.common.Language
import kpn.api.common.Languages
import kpn.api.common.PoiDetail
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoiSummaryPage
import kpn.api.common.poi.LocationPoisPage
import kpn.api.common.poi.PoiLocationsPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.CurrentUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PoiController(poiFacade: PoiFacade) {

  @GetMapping(value = Array("/api/poi/areas"))
  def areas(): ApiResponse[String] = {
    poiFacade.areas()
  }

  @GetMapping(value = Array("/api/poi-detail/{elementType:node|way|relation}/{elementId}"))
  def getPoiDetail(
    @RequestParam language: String,
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiDetail] = {
    poiFacade.getPoiDetail(
      toLanguage(language),
      PoiRef(elementType, elementId)
    )
  }

  @PostMapping(value = Array("/api/pois/{country}/{location}"))
  def locationPois(
    @RequestParam language: String,
    @PathVariable country: Country,
    @PathVariable location: String,
    @RequestBody parameters: LocationPoiParameters,
    @RequestParam layers: String
  ): ApiResponse[LocationPoisPage] = {
    val locationKey = LocationKey(NetworkType.hiking, country, location)
    poiFacade.locationPois(
      CurrentUser.name,
      toLanguage(language),
      locationKey,
      parameters,
      layers
    )
  }

  @PostMapping(value = Array("/api/pois/{country}/{location}/summary"))
  def locationPoiSummary(
    @RequestParam language: String,
    @PathVariable country: Country,
    @PathVariable location: String
  ): ApiResponse[LocationPoiSummaryPage] = {
    val locationKey = LocationKey(NetworkType.hiking, country, location)
    poiFacade.locationPoiSummary(
      CurrentUser.name,
      toLanguage(language),
      locationKey
    )
  }

  @GetMapping(value = Array("/api/poi/locations/{country}/{language}"))
  def locations(
    @PathVariable country: Country,
    @PathVariable language: String,
  ): ApiResponse[PoiLocationsPage] = {
    poiFacade.locations(CurrentUser.name, country, toLanguage(language))
  }

  private def toLanguage(language: String): Language = {
    Languages.all.find(_.toString.toLowerCase == language).getOrElse(EN)
  }
}
