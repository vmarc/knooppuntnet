package kpn.server.analyzer.engine.analysis.network

import kpn.api.custom.Timestamp

trait BulkNetworkAnalyzer {

  def analyze(timestamp: Timestamp, networkIds: Seq[Long]): Seq[Long]

}
