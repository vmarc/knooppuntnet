package kpn.core.poi

import kpn.api.common.poi.Poi

trait PoiLoader {
  def load(elementType: String, layer: String, bbox: String, condition: String): Seq[Poi]
}
