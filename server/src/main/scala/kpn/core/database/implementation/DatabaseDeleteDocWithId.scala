package kpn.core.database.implementation

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.ResourceAccessException

class DatabaseDeleteDocWithId(context: DatabaseContext) {

  def deleteDocWithId(docId: String, rev: String): Unit = {

    val url = s"${context.databaseUrl}/$docId?rev=$rev"

    try {
      context.authenticatedRestTemplate.delete(url)
    }
    catch {
      case e: ResourceAccessException =>
        throw new IllegalStateException(s"Could not delete doc '$url' (invalid user/password?)", e)

      case e: HttpClientErrorException =>
        if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode)) {
          throw new IllegalStateException(s"Could not delete doc '$url' (invalid user/password?)", e)
        }
        if (HttpStatus.NOT_FOUND.equals(e.getStatusCode)) {
          // ignore: document does not exist anymore in the mean while, perhaps deleted from other thread?
        }
        else {
          throw new IllegalStateException(s"Could not delete doc '$url'", e)
        }
    }
  }

}
