package kpn.core.database.implementation

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

object DatabaseAllIds {
  case class ViewResult(rows: Seq[ViewResultRow])

  case class ViewResultRow(id: String)
}

class DatabaseAllIds(context: DatabaseContext) {

  def execute(stale: Boolean = true): Seq[String] = {
    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_all_docs")
    b.queryParam("include_docs", "false")
    if (stale) {
      b.queryParam("stale", "ok")
    }
    val url = b.toUriString
    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String]("{}", headers)
    val response: ResponseEntity[String] = context.restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    val result = context.objectMapper.readValue(response.getBody, classOf[DatabaseAllIds.ViewResult])
    result.rows.map(_.id)
  }
}
