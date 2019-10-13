package kpn.server.config

import akka.actor.ActorSystem
import kpn.core.app.ActorSystemConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServerConfiguration {

  @Bean
  def system: ActorSystem = ActorSystemConfig.actorSystem()

}
