package kpn.core.database.implementation

import kpn.core.db.couch.Keys
import kpn.core.db.couch.ViewResult2
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

class DatabaseKeysWithIds(context: DatabaseContext) {

  def keysWithIds(docIds: Seq[String], stale: Boolean): Seq[String] = {

    val body = context.objectMapper.writeValueAsString(Keys(docIds))

    val restTemplate = new RestTemplate

    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_all_docs")
    b.queryParam("include_docs", "false")
    if (stale) {
      b.queryParam("stale", "ok")
    }
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    val result = context.objectMapper.readValue(response.getBody, classOf[ViewResult2])
    result.rows.flatMap(_.id)
  }
}
