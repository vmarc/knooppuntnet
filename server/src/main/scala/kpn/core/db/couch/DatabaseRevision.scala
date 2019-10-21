package kpn.core.db.couch

import kpn.core.util.Util
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

class DatabaseRevision(context: DatabaseContext) {

  def revision(docId: String): Option[String] = {

    val url = s"${context.databaseUrl}/${docId}"

    val restTemplate = new RestTemplate

    try {
      val entity = new HttpEntity[String]("")
      val response: ResponseEntity[String] = restTemplate.exchange(url, HttpMethod.HEAD, entity, classOf[String])
      Some(Util.withoutQuotes(response.getHeaders.getETag))
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not get document revision '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          None
        }
        else {
          throw e
        }
    }
  }

}
