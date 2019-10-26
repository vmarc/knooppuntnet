package kpn.core.database.implementation

import kpn.core.database.doc.Doc
import kpn.core.database.implementation.DatabaseBulkSave.SaveRequest
import kpn.core.util.Log
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.util.UriComponentsBuilder

object DatabaseBulkSave {

  private val log = Log(classOf[DatabaseBulkSave])

  case class SaveRequest(
    all_or_nothing: String = "false",
    docs: Seq[Doc]
  )

  case class SaveDocResult(
    id: String,
    ok: Option[Boolean],
    rev: Option[String],
    error: Option[String],
    reason: Option[String]
  )

  case class SaveResponse(results: Seq[SaveDocResult])

}

import kpn.core.database.implementation.DatabaseBulkSave._

class DatabaseBulkSave(context: DatabaseContext) {

  def bulkSave[T](docs: Seq[Doc]): Unit = {

    val body = context.objectMapper.writeValueAsString(SaveRequest(docs = docs))
    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_bulk_docs")
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)

    try {
      val response: ResponseEntity[String] = context.authenticatedRestTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
      val saveResponse = context.objectMapper.readValue(s"""{"results": ${response.getBody}}""", classOf[SaveResponse])

      if (saveResponse.results.exists(!_.ok.contains(true))) {
        log.error(s"Could not save documents\nrequest=$body\nresponse=${response.getBody}")
      }
    }
    catch {
      case e: HttpClientErrorException =>
        throw new IllegalStateException(s"Could not save documents\nrequest=$body\nresponse=${e.getResponseBodyAsString}", e)
    }
  }
}
