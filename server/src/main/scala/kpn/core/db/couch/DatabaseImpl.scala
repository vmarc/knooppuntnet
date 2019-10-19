package kpn.core.db.couch

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class DatabaseImpl(couchConfig: CouchConfig, objectMapper: ObjectMapper, name: String) extends Database {

  override def exists: Boolean = {
    val restTemplate = new RestTemplate
    try {
      restTemplate.headForHeaders(s"$url")
      true
    }
    catch {
      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          false
        }
        else {
          throw e
        }
    }
  }

  override def docWithId[T](id: String, docType: Class[T]): Option[T] = {
    val restTemplate = new RestTemplate
    val response = restTemplate.getForObject(s"$url/$id", classOf[String])
    Some(objectMapper.readValue(response, docType))
  }

  private def url: String = {
    s"http://${couchConfig.host}:${couchConfig.port}/${name}"
  }

}
