package kpn.core.database.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.db.couch.CouchConfig
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.web.client.RestTemplate

case class DatabaseContext(couchConfig: CouchConfig, objectMapper: ObjectMapper, databaseName: String) {

  def databaseUrl: String = {
    s"http://${couchConfig.host}:${couchConfig.port}/$databaseName"
  }

  def authenticatedRestTemplate: RestTemplate = {
    val restTemplate = new RestTemplate
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )
    restTemplate
  }

}
