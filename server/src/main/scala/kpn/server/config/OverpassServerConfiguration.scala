package kpn.server.config

import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile(Array("overpass"))
class OverpassServerConfiguration {

  @Bean
  def threadMetrics = new JvmThreadMetrics

  @Bean
  def testEnabled(@Value("${app.test-enabled:false}") value: Boolean): Boolean = {
    value
  }
}
