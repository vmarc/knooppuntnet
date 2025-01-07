package kpn.server.search

import kpn.api.common.SearchResponse
import kpn.api.common.search.ConditionGroup
import kpn.api.common.search.RouteList

trait SearchFacade {
  def search(query: String): Option[SearchResponse]

  def explore(query: ConditionGroup): RouteList
}
