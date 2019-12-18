package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon

trait PoiScopeAnalyzer {
  def inScope(latLon: LatLon): Boolean
}
