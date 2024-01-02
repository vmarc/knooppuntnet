package kpn.server.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession
import org.springframework.session.data.mongo.JacksonMongoSessionConverter

@Configuration(proxyBeanMethods = false)
@EnableMongoHttpSession
class HttpSessionConfig {

  @Bean
  def mongoSessionConverter = {
    new JacksonMongoSessionConverter()
  }
}
