package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object ApplicationAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (record.path == "/" || record.path.endsWith(".js.map") || record.path.endsWith(".js") || record.path.endsWith(".css") || record.path.endsWith("ngsw.json")) {
      context.withValue("application").copy(recordAnalysis = context.recordAnalysis.copy(application = true))
    }
    else {
      context
    }
  }
}
