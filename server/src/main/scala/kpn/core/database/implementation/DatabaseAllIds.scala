package kpn.core.database.implementation

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder

object DatabaseAllIds {
  case class ViewResult(rows: Seq[ViewResultRow])

  case class ViewResultRow(id: String)
}

class DatabaseAllIds(context: DatabaseContext) {

  def execute(): Seq[String] = {
    Seq.empty
  }
}
