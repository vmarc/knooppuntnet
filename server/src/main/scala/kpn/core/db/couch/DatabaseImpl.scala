package kpn.core.db.couch

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

class DatabaseImpl(couchConfig: CouchConfig, objectMapper: ObjectMapper, val name: String) extends Database {

  override def exists: Boolean = {
    val restTemplate = new RestTemplate
    try {
      restTemplate.headForHeaders(url)
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

  def create(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    );

    try {
      restTemplate.put(url, "")
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not create database '$url' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.PRECONDITION_FAILED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not create database '$url' (already exists?)", e)
        }
        else {
          throw e;
        }
    }
  }

  def delete(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    );

    try {
      restTemplate.delete(url)
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not delete database '$url' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete database '$url' (database does not exist?)", e)
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
