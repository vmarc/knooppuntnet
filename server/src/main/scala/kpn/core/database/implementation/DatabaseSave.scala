package kpn.core.database.implementation

import kpn.core.database.doc.Doc
import kpn.core.util.Log
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseSave(context: DatabaseContext, maxRetries: Int = 3, backOffPeriod: Long = 5000) {

  private val log = Log(classOf[DatabaseSave])

  def save[T](doc: Doc): Unit = {

    var retry = true
    var retryCount = 0
    val url = s"${context.databaseUrl}/${doc._id}"

    while (retry && retryCount < maxRetries) {

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

    if (retryCount >= maxRetries) {
      throw new IllegalStateException(s"Could not save '$url' after $maxRetries retries")
    }
  }

  private def readOldDoc[T](url: String, docType: Class[T]): Option[T] = {

    var retry = true
    var retryCount = 0
    var result: Option[T] = None

    while (retry && retryCount < maxRetries) {
      retry = false
      try {
        val response = context.restTemplate.getForObject(url, classOf[String])
        result = Some(context.objectMapper.readValue(response, docType))
      }
      catch {
        case e: HttpServerErrorException.InternalServerError =>
          if (e.getMessage.contains("No DB shards could be opened")) {
            log.warn(s"No DB shards could be opened - will retry get document '${context.databaseUrl}'")
            Thread.sleep(backOffPeriod)
            retryCount = retryCount + 1
            retry = true
          }
          else {
            throw new IllegalStateException(s"Could not get document '${context.databaseUrl}'", e)
          }

        case e: ResourceAccessException =>
          throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' (invalid user/password?)", e)

        case e: HttpClientErrorException =>
          if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
            throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' (invalid user/password?)", e)
          }
          if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
            result = None
          }
          else {
            throw e
          }
      }
    }
    if (retryCount >= maxRetries) {
      throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' after $maxRetries retries")
    }
    result
  }

  private def writeDoc(url: String, newDoc: Doc): Boolean = {
    val json = context.objectMapper.writeValueAsString(newDoc)
    try {
      val entity = new HttpEntity[String](json)
      val response: ResponseEntity[String] = context.restTemplate.exchange(url, HttpMethod.PUT, entity, classOf[String])
      if (!HttpStatus.CREATED.equals(response.getStatusCode)) {
        throw new IllegalStateException(s"Could not save '$url' (unexpected status code ${response.getStatusCode.name()})")
      }
      true
    }
    catch {
      case e: HttpServerErrorException.InternalServerError =>
        if (e.getMessage.contains("No DB shards could be opened")) {
          log.warn(s"No DB shards could be opened - will retry  save '$url'")
          Thread.sleep(backOffPeriod)
          false
        }
        else {
          throw new IllegalStateException(s"Could not save '$url'", e)
        }

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
