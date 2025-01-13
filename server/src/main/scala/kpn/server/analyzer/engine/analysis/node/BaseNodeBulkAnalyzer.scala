package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.core.doc.BaseNodeDoc

trait BaseNodeBulkAnalyzer {

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[BaseNodeDoc]
}
