package kpn.server.analyzer.engine.poi

import kpn.server.analyzer.engine.changes.changes.OsmChange

trait PoiChangeAnalyzer {
  def analyze(osmChange: OsmChange): Unit
}
