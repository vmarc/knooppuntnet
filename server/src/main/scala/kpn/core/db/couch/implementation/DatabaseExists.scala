package kpn.core.db.couch.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate

class DatabaseExists(context: DatabaseContext) {

  def exists: Boolean = {
    val restTemplate = new RestTemplate
    try {
      restTemplate.headForHeaders(context.databaseUrl)
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

}
