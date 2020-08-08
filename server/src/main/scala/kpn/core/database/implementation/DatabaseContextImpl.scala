package kpn.core.database.implementation

import java.nio.charset.Charset

import com.fasterxml.jackson.databind.ObjectMapper
import kpn.core.db.couch.CouchConfig
import org.springframework.http.client.support.BasicAuthenticationInterceptor
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate

case class DatabaseContextImpl(
  couchConfig: CouchConfig,
  objectMapper: ObjectMapper,
  databaseName: String
) extends DatabaseContext {

  def databaseUrl: String = {
    s"http://${couchConfig.host}:${couchConfig.port}/$databaseName"
  }

  def restTemplate: RestOperations = {
    val restTemplate = new RestTemplate(converters)
    restTemplate.getInterceptors.add(
      new BasicAuthenticationInterceptor(couchConfig.user, couchConfig.password)
    )
    restTemplate
  }

  private def converters: java.util.List[HttpMessageConverter[_]] = {
    java.util.Arrays.asList(new StringHttpMessageConverter(Charset.forName("UTF-8")))
  }
}
