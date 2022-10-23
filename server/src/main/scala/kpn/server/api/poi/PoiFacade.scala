package kpn.server.api.poi

import kpn.api.common.Language
import kpn.api.common.PoiDetail
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoiSummaryPage
import kpn.api.common.poi.LocationPoisPage
import kpn.api.common.poi.PoiLocationsPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiFacade {

  def areas(): ApiResponse[String]

  def getPoiDetail(language: Language, poiRef: PoiRef): ApiResponse[PoiDetail]

  def locationPois(
    user: Option[String],
    language: Language,
    location: String,
    parameters: LocationPoiParameters,
    layers: String
  ): ApiResponse[LocationPoisPage]

  def locationPoiSummary(
    user: Option[String],
    language: Language,
    location: String
  ): ApiResponse[LocationPoiSummaryPage]

  def locations(
    user: Option[String],
    country: Country,
    language: Language
  ): ApiResponse[PoiLocationsPage]
}
