package kpn.server.analyzer.engine.analysis.node

import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.NodeDoc

trait BulkNodeAnalyzer {

  def analyze(timestamp: Timestamp, nodeIds: Seq[Long]): Seq[NodeDoc]

}
