package kpn.server.search

import kpn.api.common.SearchResponse
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteSearchResult
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class SearchFacadeImpl(geocoder: Geocoder, routeRepository: RouteRepository) extends SearchFacade {
  override def search(query: String): Option[SearchResponse] = {
    val geocodeLocations = geocoder.search(query)
    Some(
      SearchResponse(
        geocodeLocations
      )
    )
  }

  override def explore(query: ConditionGroup): Seq[RouteSearchResult] = {
    routeRepository.explore(query)
  }
}
