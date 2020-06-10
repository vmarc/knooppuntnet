package kpn.core.database.implementation

import kpn.core.util.Log
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseDocWithId(context: DatabaseContext, maxRetries: Int = 3, backOffPeriod: Long = 5000) {

  private val log = Log(classOf[DatabaseDocWithId])

  def docWithId[T](id: String, docType: Class[T]): Option[T] = {

    var retry = true
    var retryCount = 0
    var result: Option[T] = None

    while (retry && retryCount < maxRetries) {
      retry = false
      val url = s"${context.databaseUrl}/$id"
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
}
