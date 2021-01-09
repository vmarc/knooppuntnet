package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object ApiAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (record.path.startsWith("/api/")) {
      val key = if (context.recordAnalysis.robot) "api-robot" else "api"
      context.withValue(key).copy(recordAnalysis = context.recordAnalysis.copy(api = true))
    }
    else {
      context
    }
  }
}
