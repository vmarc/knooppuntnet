package kpn.core.database.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseDocWithId(context: DatabaseContext) {

  def docWithId[T](id: String, docType: Class[T]): Option[T] = {
    val url = s"${context.databaseUrl}/$id"
    try {
      val response = context.restTemplate.getForObject(url, classOf[String])
      Some(context.objectMapper.readValue(response, docType))
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not get document '${context.databaseUrl}' (invalid user/password?)", e)

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
