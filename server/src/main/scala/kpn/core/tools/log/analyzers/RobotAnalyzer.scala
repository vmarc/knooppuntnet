package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object RobotAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (isRobot(record)) {
      context.withValue("robot").copy(recordAnalysis = context.recordAnalysis.copy(robot = true))
    }
    else {
      context.withValue("non-robot")
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
