package kpn.server.analyzer.engine.analysis.node

import kpn.core.doc.NodeDoc

trait BulkNodeAnalyzer {

  def analyze(nodeIds: Seq[Long]): Seq[NodeDoc]
}
