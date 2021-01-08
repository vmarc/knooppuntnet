package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

object ApplicationAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis = {
    if (record.path == "/" || record.path.endsWith(".js.map") || record.path.endsWith(".js") || record.path.endsWith(".css") || record.path.endsWith("ngsw.json")) {
      analysis.copy(application = true)
    }
    else {
      analysis
    }
  }
}
