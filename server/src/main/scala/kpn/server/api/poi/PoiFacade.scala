package kpn.server.api.poi

import kpn.api.common.PoiDetail
import kpn.api.custom.ApiResponse
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiFacade {

  def areas(): ApiResponse[String]

  def getPoiDetail(poiRef: PoiRef): ApiResponse[PoiDetail]
}
