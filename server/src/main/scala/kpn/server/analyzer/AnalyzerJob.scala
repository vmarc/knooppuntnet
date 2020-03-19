package kpn.server.analyzer

import org.springframework.scheduling.annotation.Scheduled

class AnalyzerJob(analyzer: Analyzer) {

  var init = false

  @Scheduled(fixedDelay = 5000)
  def analyze(): Unit = {
    if (!init) {
      analyzer.load()
      init = true
    }
    analyzer.process()
  }
}
