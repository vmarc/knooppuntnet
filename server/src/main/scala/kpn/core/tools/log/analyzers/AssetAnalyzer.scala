package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object AssetAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (record.path.contains("/assets/") || record.path.endsWith("favicon.ico")) {
      val key = if (context.recordAnalysis.robot) "asset-robot" else "asset"
      context.withValue(key).copy(recordAnalysis = context.recordAnalysis.copy(asset = true))
    }
    else {
      context
    }
  }

}
