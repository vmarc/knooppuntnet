package kpn.core.database.implementation

import com.fasterxml.jackson.core.JsonProcessingException
import kpn.core.database.query.Query
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException

class DatabaseQuery(context: DatabaseContext) {

  def execute[T](query: Query[T]): T = {

    val queryString = query.build()
    val url = s"${context.databaseUrl}/$queryString"
    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE)
    val entity = new HttpEntity[String]("", headers)
    try {
      val response: ResponseEntity[String] = context.restTemplate.exchange(url, HttpMethod.GET, entity, classOf[String])
      try {
        context.objectMapper.readValue(response.getBody, query.docType)
      }
      catch {
        case e: JsonProcessingException =>
          throw new IllegalStateException(s"Could not process response for query $url\n response=${response.getBody}", e)
      }
    }
    catch {
      case e: HttpClientErrorException.BadRequest =>
        throw new IllegalStateException(s"Could not execute query $url\n${e.getStatusText}\n${e.getResponseBodyAsString}", e)
    }
  }
}
