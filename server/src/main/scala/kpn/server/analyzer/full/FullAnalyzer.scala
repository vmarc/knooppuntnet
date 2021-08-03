package kpn.server.analyzer.full

import kpn.api.custom.Timestamp

trait FullAnalyzer {

  def analyze(timestamp: Timestamp): Unit

}
