package kpn.core.db.couch

import org.springframework.http.HttpStatus
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.client.RestTemplate

class DatabaseDelete(context: DatabaseContext) {

  def delete(): Unit = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(context.couchConfig.user, context.couchConfig.password)
    )

    try {
      restTemplate.delete(context.databaseUrl)
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not delete database '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete database '${context.databaseUrl}' (database does not exist?)", e)
        }
        else {
          throw e
        }
    }
  }

}
