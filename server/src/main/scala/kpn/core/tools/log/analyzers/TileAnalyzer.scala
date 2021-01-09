package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object TileAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (record.path.startsWith("/tiles/")) {
      val key = if (context.recordAnalysis.robot) "tile-robot" else "tile"
      context.withValue(key).copy(recordAnalysis = context.recordAnalysis.copy(tile = true))
    }
    else {
      context
    }
  }

}
