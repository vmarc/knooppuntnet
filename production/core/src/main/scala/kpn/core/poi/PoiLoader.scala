package kpn.core.poi

trait PoiLoader {
  def load(elementType: String, layer: String, bbox: String, condition: String): Seq[Poi]
}
