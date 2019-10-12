package kpn.server.analyzer

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class AnalyzerContainer(analyzer: Analyzer) {

  @Bean
  @ConditionalOnProperty(value = Array("analyzer.enabled"), matchIfMissing = false, havingValue = "true")
  def analyzer(): AnalyzerJob = {
    new AnalyzerJob(analyzer)
  }

}
