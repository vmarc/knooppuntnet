package kpn.server.search

import kpn.api.common.SearchResponse

trait SearchFacade {
  def search(query: String): Option[SearchResponse]
}
