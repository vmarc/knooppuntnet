package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogRecord
import kpn.core.tools.log.LogRecordAnalysis

trait LogRecordAnalyzer {
  def analyze(record: LogRecord, analysis: LogRecordAnalysis): LogRecordAnalysis
}
