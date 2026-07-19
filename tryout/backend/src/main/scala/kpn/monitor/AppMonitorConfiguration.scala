package kpn.monitor

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class AppMonitorConfiguration {

  @Bean
  def applicationName(@Value("${app.name:monitor}") value: String): String = {
    value
  }

  @Bean
  def mailSender: JavaMailSender = {
    new JavaMailSenderImpl()
  }
}
