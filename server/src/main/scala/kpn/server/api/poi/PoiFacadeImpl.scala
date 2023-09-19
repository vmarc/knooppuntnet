package kpn.server.api.poi

import kpn.api.common.Language
import kpn.api.common.PoiAnalysis
import kpn.api.common.PoiDetail
import kpn.api.common.PoiState
import kpn.api.common.location.Location
import kpn.api.common.poi.LocationPoiParameters
import kpn.api.common.poi.LocationPoisPage
import kpn.api.common.poi.LocationPoiSummaryPage
import kpn.api.common.poi.PoiLocationsPage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Country
import kpn.core.common.TimestampLocal
import kpn.core.poi.PoiLocationGeoJson
import kpn.database.base.Database
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.LocationPoisPageBuilder
import kpn.server.api.analysis.pages.LocationPoiSummaryPageBuilder
import kpn.server.api.analysis.pages.PoiLocationsPageBuilder
import kpn.server.api.analysis.pages.PoiPageBuilder
import org.springframework.stereotype.Component

@Component
class PoiFacadeImpl(
  database: Database,
  poiPageBuilder: PoiPageBuilder,
  locationPoisPageBuilder: LocationPoisPageBuilder,
  locationPoiSummaryPageBuilder: LocationPoiSummaryPageBuilder,
  poiLocationsPageBuilder: PoiLocationsPageBuilder,
  locationService: LocationService
) extends PoiFacade {

  override def areas(): ApiResponse[String] = {
    val geoJson = new PoiLocationGeoJson().geoJsonString()
    val response = ApiResponse(null, 1, Some(geoJson))
    TimestampLocal.localize(response)
    response
  }

  override def getPoiDetail(language: Language, poiRef: PoiRef): ApiResponse[PoiDetail] = {
    val poiDetailOption = database.pois.findByStringId(poiRef.toId).map { poi =>
      val locationNames = poi.location.names.map(locationId => locationService.name(language, locationId))
      val enrichedPoi = poi.copy(
        location = Location(locationNames)
      )
      database.poiStates.findByStringId(poiRef.toId) match {
        case None =>
          poiPageBuilder.build(poiRef) match {
            case None => PoiDetail(enrichedPoi, PoiAnalysis(), PoiState(poiRef.toId))
            case Some(poiPage) => PoiDetail(enrichedPoi, poiPage.analysis, PoiState(poiRef.toId))
          }
        case Some(poiState) =>
          poiPageBuilder.build(poiRef) match {
            case None => PoiDetail(enrichedPoi, PoiAnalysis(), poiState)
            case Some(poiPage) => PoiDetail(enrichedPoi, poiPage.analysis, poiState)
          }
      }
    }
    ApiResponse(null, 1, poiDetailOption)
  }

  override def locationPois(
    user: Option[String],
    language: Language,
    location: String,
    parameters: LocationPoiParameters,
    layers: String
  ): ApiResponse[LocationPoisPage] = {
    val page = locationPoisPageBuilder.build(language, location, parameters, layers.split(",").toSeq)
    ApiResponse(null, 1, Some(page))
  }

  override def locationPoiSummary(
    user: Option[String],
    language: Language,
    location: String
  ): ApiResponse[LocationPoiSummaryPage] = {
    val page = locationPoiSummaryPageBuilder.build(language, location)
    ApiResponse(null, 1, Some(page))
  }

  override def locations(
    user: Option[String],
    country: Country,
    language: Language
  ): ApiResponse[PoiLocationsPage] = {
    val page = poiLocationsPageBuilder.build(country, language)
    ApiResponse(null, 1, Some(page))
  }
}
