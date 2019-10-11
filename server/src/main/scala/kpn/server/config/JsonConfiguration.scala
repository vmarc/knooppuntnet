package kpn.server.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonConfiguration {

  @Bean
  def defaultScalaModule: Module = DefaultScalaModule


  import org.springframework.context.annotation.Bean

//  @Bean
//  def securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http.authorizeExchange.pathMatchers("/actuator/**").permitAll.anyExchange.authenticated.and.build
  import org.springframework.context.annotation.Bean
  import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics

  @Bean def threadMetrics = new JvmThreadMetrics
}
