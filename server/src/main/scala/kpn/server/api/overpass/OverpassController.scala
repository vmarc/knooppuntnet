package kpn.server.api.overpass

import kpn.core.overpass.OverpassQueryExecutor
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OverpassController(nonCachingOverpassQueryExecutor: OverpassQueryExecutor) {

  @PostMapping(value = Array("/api/overpass"))
  def mapNodeDetail(
    @RequestBody queryString: String
  ): String = {
    nonCachingOverpassQueryExecutor.execute(queryString)
  }
}
