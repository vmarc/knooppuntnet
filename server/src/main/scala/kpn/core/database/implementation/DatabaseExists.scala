package kpn.core.database.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException

class DatabaseExists(context: DatabaseContext) {

  def exists: Boolean = {
    try {
      context.restTemplate.headForHeaders(context.databaseUrl)
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
