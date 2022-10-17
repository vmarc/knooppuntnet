package kpn.server.api.poi

import kpn.api.common.EN
import kpn.api.common.Language
import kpn.api.common.Languages
import kpn.api.common.PoiDetail
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoisPage
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
    @PathVariable elementType: String,
    @PathVariable elementId: Long
  ): ApiResponse[PoiDetail] = {
    poiFacade.getPoiDetail(PoiRef(elementType, elementId))
  }

  @PostMapping(value = Array("/api/pois/{country}/{location}"))
  def locationNodes(
    @RequestParam language: String,
    @PathVariable country: Country,
    @PathVariable location: String,
    @RequestBody parameters: LocationPoiParameters
  ): ApiResponse[LocationPoisPage] = {
    val locationKey = LocationKey(NetworkType.hiking, country, location)
    poiFacade.locationPois(CurrentUser.name, toLanguage(language), locationKey, parameters)
  }

  private def toLanguage(language: String): Language = {
    Languages.all.find(_.toString.toLowerCase == language).getOrElse(EN)
  }
}
