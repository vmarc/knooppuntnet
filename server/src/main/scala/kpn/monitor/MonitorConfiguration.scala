package kpn.monitor

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MonitorConfiguration() {

  @Bean
  def applicationName(@Value("${app.name:monitor}") value: String): String = {
    value
  }

}
