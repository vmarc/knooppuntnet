package kpn.server.api.poi

import kpn.api.common.PoiAnalysis
import kpn.api.common.PoiDetail
import kpn.api.common.PoiState
import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.core.poi.PoiLocationGeoJson
import kpn.database.base.Database
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.api.analysis.pages.PoiPageBuilder
import org.springframework.stereotype.Component

@Component
class PoiFacadeImpl(database: Database, poiPageBuilder: PoiPageBuilder) extends PoiFacade {

  override def areas(): ApiResponse[String] = {
    val geoJson = new PoiLocationGeoJson().geoJsonString()
    val response = ApiResponse(null, 1, Some(geoJson))
    TimestampLocal.localize(response)
    response
  }

  override def getPoiDetail(poiRef: PoiRef): ApiResponse[PoiDetail] = {
    val poiDetailOption = database.pois.findByStringId(poiRef.toId).map { poi =>
      database.poiStates.findByStringId(poiRef.toId) match {
        case None =>
          poiPageBuilder.build(poiRef) match {
            case None => PoiDetail(poi, PoiAnalysis(), PoiState(poiRef.toId))
            case Some(poiPage) => PoiDetail(poi, poiPage.analysis, PoiState(poiRef.toId))
          }
        case Some(poiState) =>
          poiPageBuilder.build(poiRef) match {
            case None => PoiDetail(poi, PoiAnalysis(), poiState)
            case Some(poiPage) => PoiDetail(poi, poiPage.analysis, poiState)
          }
      }
    }
    ApiResponse(null, 1, poiDetailOption)
  }
}
