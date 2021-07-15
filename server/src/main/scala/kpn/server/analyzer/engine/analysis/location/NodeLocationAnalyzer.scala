package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location

trait NodeLocationAnalyzer {
  def locations(latitude: String, longitude: String): Seq[String]

  def oldLocate(latitude: String, longitude: String): Option[Location]
}
