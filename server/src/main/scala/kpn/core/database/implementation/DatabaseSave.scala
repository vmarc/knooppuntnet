package kpn.core.database.implementation

import kpn.core.database.doc.Doc
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseSave(context: DatabaseContext) {

  def save[T](doc: Doc): Unit = {

    var retry = true
    var retryCount = 0
    val url = s"${context.databaseUrl}/${doc._id}"

    while (retry && retryCount < 3) {

      val oldDoc = readOldDoc(url, doc.getClass)

      val newDocOption = oldDoc match {
        case None => Some(doc)
        case Some(oldDoc) =>
          if (!doc.equals(oldDoc.withRev(None))) {
            Some(doc.withRev(oldDoc._rev))
          }
          else {
            None
          }
      }

      newDocOption match {
        case None => retry = false
        case Some(newDoc) =>
          if (writeDoc(url, newDoc)) {
            retry = false
          }
          else {
            retryCount = retryCount + 1
          }
      }
    }

    if (retryCount >= 3) {
      throw new IllegalStateException(s"Could not save '$url' after 3 retries")
    }
  }

  private def readOldDoc[T](url: String, docType: Class[T]): Option[T] = {
    try {
      val response = context.restTemplate.getForObject(url, classOf[String])
      Some(context.objectMapper.readValue(response, docType))
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' (invalid user/password?)", e)
        }
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          None
        }
        else {
          throw e
        }
    }
  }

  private def writeDoc(url: String, newDoc: Doc): Boolean = {
    val json = context.objectMapper.writeValueAsString(newDoc)
    try {
      val entity = new HttpEntity[String](json)
      val response: ResponseEntity[String] = context.authenticatedRestTemplate.exchange(url, HttpMethod.PUT, entity, classOf[String])
      if (!HttpStatus.CREATED.equals(response.getStatusCode)) {
        throw new IllegalStateException(s"Could not save '$url' (unexpected status code ${response.getStatusCode.name()})")
      }
      true
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not save '$url' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.CONFLICT.equals(e.getStatusCode)) {
          false
        }
        else {
          if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
            throw new IllegalStateException(s"Could not save '$url' (invalid user/password?)", e)
          }
          throw new IllegalStateException(s"Could not save '$url'", e)
        }
    }
  }
}
