package kpn.server.analyzer

import java.text.SimpleDateFormat
import java.util.Date

import kpn.core.util.Log
import org.springframework.stereotype.Component

@Component
class AnalyzerImpl extends Analyzer {

  private val log = Log(classOf[AnalyzerImpl])

  def load(): Unit = {
    log.info(s"Load $now")
    Thread.sleep(10000)
    log.info(s"End Load $now")
  }

  def process(): Unit = {
    log.info(s"Process $now")
    Thread.sleep(10000)
    log.info(s"Process $now")

  }

  private def now: String = {
    new SimpleDateFormat("HH:mm:ss").format(new Date)
  }

}
