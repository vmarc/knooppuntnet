package kpn.server.search

import kpn.api.common.SearchResponse
import org.springframework.stereotype.Component

@Component
class SearchFacadeImpl(geocoder: Geocoder) extends SearchFacade {
  override def search(query: String): Option[SearchResponse] = {
    val geocodeLocations = geocoder.search(query)
    Some(
      SearchResponse(
        geocodeLocations
      )
    )
  }
}
