package kpn.server.api.overpass

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.util.Log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OverpassController(nonCachingOverpassQueryExecutor: OverpassQueryExecutor) {

  private val log = Log(classOf[OverpassController])

  @PostMapping(value = Array("/api/overpass"))
  def mapNodeDetail(@RequestBody queryString: String): String = {
    log.infoElapsed {
      val xml = try {
        nonCachingOverpassQueryExecutor.execute(queryString)
      }
      catch {
        case e: Exception =>
          log.error(e.getMessage)
          throw e
      }
      val message = shorten(queryString)
      (message, xml)
    }
  }

  private def shorten(queryString: String): String = {
    val string = queryString
      .replaceAll("\\[timeout:1500]\\[maxsize:24000000000];", "")
      .replaceAll("\\[timeout:500]\\[maxsize:12000000000];", "")
    if (string.length > 300) {
      string.take(300) + "..."
    }
    else {
      string
    }
  }

}
