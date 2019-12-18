package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon

trait PoiQueryExecutor {
  def center(poiRef: PoiRef): LatLon
}
