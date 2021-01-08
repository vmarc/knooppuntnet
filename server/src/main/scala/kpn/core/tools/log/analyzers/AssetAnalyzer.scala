package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

object AssetAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (record.path.contains("/assets/") || record.path.endsWith("favicon.ico")) {
      analysis.copy(asset = true)
    }
    else {
      analysis
    }
  }

}
