package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

trait LogRecordAnalyzer {
  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext
}
