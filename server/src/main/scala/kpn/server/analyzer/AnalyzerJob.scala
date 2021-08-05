package kpn.server.analyzer

import kpn.core.util.Log
import kpn.server.config.Mailer
import org.springframework.scheduling.annotation.Scheduled

class AnalyzerJob(applicationName: String, analyzer: Analyzer, mailer: Mailer) {

  private val log = Log(classOf[AnalyzerJob])

  var init = false
  var aborted = false

  @Scheduled(fixedDelay = 5000)
  def analyze(): Unit = {
    if (!aborted) {
      doAnalyze()
    }
  }

  private def doAnalyze(): Unit = {
    if (!init) {
      load()
    }
    // process()
  }

  private def load(): Unit = {
    try {
      analyzer.load()
    }
    catch {
      case e: Exception =>
        log.error("Aborted during initial load", e)
        mailer.send(s"$applicationName analyzer aborted during initial load", e.getStackTrace.mkString("\n"))
        aborted = true
    }
    init = true
  }

  private def process(): Unit = {
    try {
      analyzer.process()
    }
    catch {
      case e: Exception =>
        log.error("Aborted", e)
        mailer.send(s"$applicationName analyzer aborted", e.getStackTrace.mkString("\n"))
        aborted = true
    }
  }
}
