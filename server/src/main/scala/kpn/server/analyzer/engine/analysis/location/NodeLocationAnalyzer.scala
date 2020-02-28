package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location

trait NodeLocationAnalyzer {
  def locate(latitude: String, longitude: String): Option[Location]
}
