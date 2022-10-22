package kpn.server.api.poi

import kpn.api.common.Language
import kpn.api.common.PoiDetail
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoiSummaryPage
import kpn.api.common.poi.LocationPoisPage
import kpn.api.common.poi.PoiLocationsPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.api.custom.LocationKey
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.CurrentUser

trait PoiFacade {

  def areas(): ApiResponse[String]

  def getPoiDetail(language: Language, poiRef: PoiRef): ApiResponse[PoiDetail]

  def locationPois(
    user: Option[String],
    language: Language,
    locationKey: LocationKey,
    parameters: LocationPoiParameters,
    layers: String
  ): ApiResponse[LocationPoisPage]

  def locationPoiSummary(
    user: Option[String],
    language: Language,
    locationKey: LocationKey
  ): ApiResponse[LocationPoiSummaryPage]

  def locations(
    user: Option[String],
    country: Country,
    language: Language
  ): ApiResponse[PoiLocationsPage]
}
