package kpn.core.poi

import kpn.shared.Poi

trait PoiLoader {
  def load(elementType: String, layer: String, bbox: String, condition: String): Seq[Poi]
}
