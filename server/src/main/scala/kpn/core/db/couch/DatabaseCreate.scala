package kpn.core.db.couch

import org.springframework.http.HttpStatus
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

class DatabaseCreate(context: DatabaseContext) {

  def create(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(context.couchConfig.user, context.couchConfig.password)
    )

    try {
      restTemplate.put(context.databaseUrl, "")
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not create database '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.PRECONDITION_FAILED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not create database '${context.databaseUrl}' (already exists?)", e)
        }
        else {
          throw e;
        }
    }
  }

}
