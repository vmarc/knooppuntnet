package kpn.core.overpass

import kpn.core.util.Log
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class OverpassQueryExecutorRemoteImpl() extends OverpassQueryExecutor {

  private val log = Log(classOf[OverpassQueryExecutorRemoteImpl])

  def execute(queryString: String): String = {
    log.debugElapsed {
      val url: String = "http://kpn:9011/api/overpass"
      val headers = new HttpHeaders()
      headers.setContentType(MediaType.TEXT_PLAIN)
      val entity = new HttpEntity[String](queryString, headers)
      val restTemplate = new RestTemplate()
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
      (queryString, response.getBody)
    }
  }
}
