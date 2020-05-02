package kpn.core.database.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseDelete(context: DatabaseContext) {

  def delete(): Unit = {

    try {
      context.authenticatedRestTemplate.delete(context.databaseUrl)
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not delete database '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete database '${context.databaseUrl}' (invalid user/password?)", e)
        }
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete database '${context.databaseUrl}' (database does not exist?)", e)
        }
        throw e
    }
  }

}
