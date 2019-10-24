package kpn.core.db.couch.implementation

import kpn.core.db.couch.Keys
import kpn.core.db.couch.ViewResult2
import kpn.core.util.Log
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

object DatabaseDeleteDocsWithIds {

  private val log = Log(classOf[DatabaseDeleteDocsWithIds])

  case class DeleteRequestDoc(_id: String, _rev: String, _deleted: Boolean = true)

  case class DeleteRequest(
    all_or_nothing: String = "false",
    docs: Seq[DeleteRequestDoc]
  )

  case class DeleteDocResult(
    id: String,
    ok: Option[Boolean],
    rev: Option[String],
    error: Option[String],
    reason: Option[String]
  )

  case class DeleteResponse(results: Seq[DeleteDocResult])

}

import kpn.core.db.couch.implementation.DatabaseDeleteDocsWithIds._

class DatabaseDeleteDocsWithIds(context: DatabaseContext) {

  def deleteDocsWithIds(docIds: Seq[String]): Unit = {

    val docs = readDocRevisions(docIds)
    val body = context.objectMapper.writeValueAsString(DeleteRequest(docs = docs))

    val restTemplate = new RestTemplate

    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_bulk_docs")
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    try {
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
      val deleteResponse = context.objectMapper.readValue(s"""{"results": ${response.getBody}}""", classOf[DeleteResponse])
      if (deleteResponse.results.exists(!_.ok.contains(true))) {
        log.error(s"Could not delete documents\nrequest=$body\nresponse=${response.getBody}")
      }
    }
    catch {
      case e: HttpClientErrorException =>
        throw new IllegalStateException(s"Could not delete documents\nrequest=$body\nresponse=${e.getResponseBodyAsString}", e)
    }
  }

  private def readDocRevisions(docIds: Seq[String]): Seq[DeleteRequestDoc] = {

    val body = context.objectMapper.writeValueAsString(Keys(docIds))

    val restTemplate = new RestTemplate

    val b = UriComponentsBuilder.fromHttpUrl(s"${context.databaseUrl}/_all_docs")
    b.queryParam("include_docs", "false")
    val url = b.toUriString

    val headers = new HttpHeaders()
    headers.setContentType(MediaType.APPLICATION_JSON)
    val entity = new HttpEntity[String](body, headers)
    val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.POST, entity, classOf[String])
    val result = context.objectMapper.readValue(response.getBody, classOf[ViewResult2])
    result.rows.flatMap { row =>
      row.id.flatMap { id =>
        row.value.map { value =>
          DeleteRequestDoc(id, value.rev)
        }
      }
    }
  }

}
