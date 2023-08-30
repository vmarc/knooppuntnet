package kpn.server.analyzer.engine.poi

import kpn.api.common.LatLon

trait PoiQueryExecutor {
  def center(poiRef: PoiRef): Option[LatLon]

  def centers(elementType: String, elementIds: Seq[Long]): Seq[ElementCenter]

}
