package kpn.server.analyzer

import kpn.server.config.Mailer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("analysis"))
class AnalyzerContainer(applicationName: String, analyzer: Analyzer, mailer: Mailer) {

  @Bean
  @ConditionalOnProperty(value = Array("app.analyzer-enabled"), matchIfMissing = false, havingValue = "true")
  def analyzerJob(): AnalyzerJob = {
    new AnalyzerJob(applicationName, analyzer, mailer)
  }
}
