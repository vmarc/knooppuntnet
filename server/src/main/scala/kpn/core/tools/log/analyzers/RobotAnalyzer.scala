package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

object RobotAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (isRobot(record)) {
      analysis.copy(robot = true)
    }
    else {
      analysis
    }
  }

  private def isRobot(record: LogRecord): Boolean = {
    val robotSignatures = Seq(
      "SemrushBot",
      "PetalBot",
      "Googlebot",
      "bingbot",
      "DuckDuckGo",
      "Applebot",
      "AhrefsBot",
      "DotBot",
      "MJ12bot",
      "RU_Bot",
      "Facebot",
      "Twitterbot"
    )

    if (record.userAgent != null) {
      robotSignatures.exists(record.userAgent.contains)
    }
    else {
      false
    }
  }

}
