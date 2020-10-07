package kpn.server.api.poi

import kpn.api.custom.ApiResponse
import kpn.core.common.TimestampLocal
import kpn.core.poi.PoiLocationGeoJson
import org.springframework.stereotype.Component

@Component
class PoiFacadeImpl extends PoiFacade {

  def areas(): ApiResponse[String] = {
    val geoJson = new PoiLocationGeoJson().geoJsonString()
    val response = ApiResponse(null, 1, Some(geoJson))
    TimestampLocal.localize(response)
    response
  }

}
