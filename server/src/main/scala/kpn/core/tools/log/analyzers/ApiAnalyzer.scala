package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

object ApiAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (record.path.startsWith("/api/")) {
      analysis.copy(api = true)
    }
    else {
      analysis
    }
  }
}
