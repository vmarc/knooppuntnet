package kpn.server.analyzer

import kpn.server.config.Mailer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class AnalyzerContainer(applicationName: String, analyzer1: Analyzer, mailer: Mailer) {

  @Bean
  @ConditionalOnProperty(value = Array("app.analyzer-enabled"), matchIfMissing = false, havingValue = "true")
  def analyzer(): AnalyzerJob = {
    new AnalyzerJob(applicationName, analyzer1, mailer)
  }
}
