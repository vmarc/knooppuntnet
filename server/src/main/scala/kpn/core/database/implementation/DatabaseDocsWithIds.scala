package kpn.core.database.implementation

import kpn.core.db.couch.Keys
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

class DatabaseDocsWithIds(context: DatabaseContext) {

  def docsWithIds[T](docIds: Seq[String], docType: Class[T]): T = {

    val body = context.objectMapper.writeValueAsString(Keys(docIds))

    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_all_docs")
    b.queryParam("include_docs", "true")
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    val response: ResponseEntity[String] = context.restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    context.objectMapper.readValue(response.getBody, docType)
  }

}
