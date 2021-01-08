package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

object TileAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (record.path.startsWith("/tiles/")) {
      analysis.copy(tile = true)
    }
    else {
      analysis
    }
  }

}
