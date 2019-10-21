package kpn.core.db.couch

import com.fasterxml.jackson.databind.ObjectMapper

case class DatabaseContext(couchConfig: CouchConfig, objectMapper: ObjectMapper, databaseName: String) {

  def databaseUrl: String = {
    s"http://${couchConfig.host}:${couchConfig.port}/$databaseName"
  }

}
