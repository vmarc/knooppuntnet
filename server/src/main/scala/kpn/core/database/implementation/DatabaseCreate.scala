package kpn.core.database.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseCreate(context: DatabaseContext) {

  def create(): Unit = {
    try {
      context.authenticatedRestTemplate.put(context.databaseUrl, "")
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not create database '${context.databaseUrl}' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not create database '${context.databaseUrl}' (invalid user/password?)", e)
        }
        if (HttpStatus.PRECONDITION_FAILED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not create database '${context.databaseUrl}' (already exists?)", e)
        }
        throw e
    }
  }

}
