package kpn.server.config

import akka.actor.ActorSystem
import com.fasterxml.jackson.databind.ObjectMapper
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import kpn.core.app.ActorSystemConfig
import kpn.server.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ServerConfiguration {

  @Bean
  @Primary
  def objectMapper: ObjectMapper = Json.objectMapper

  @Bean
  def threadMetrics = new JvmThreadMetrics

  @Bean
  def system: ActorSystem = ActorSystemConfig.actorSystem()

}
