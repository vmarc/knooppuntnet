package kpn.server.search

import kpn.api.common.SearchResponse
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class SearchFacade(geocoder: Geocoder, routeRepository: RouteRepository) {
  def search(query: String): Option[SearchResponse] = {
    val geocodeLocations = geocoder.search(query)
    Some(
      SearchResponse(
        geocodeLocations
      )
    )
  }

  def explore(query: ConditionGroup): RouteList = {
    routeRepository.explore(query)
  }
}
