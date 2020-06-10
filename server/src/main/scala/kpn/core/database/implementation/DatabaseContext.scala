package kpn.core.database.implementation

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.web.client.RestOperations

trait DatabaseContext {

  def databaseUrl: String

  def authenticatedRestTemplate: RestOperations

  def restTemplate: RestOperations

  def objectMapper: ObjectMapper
}
