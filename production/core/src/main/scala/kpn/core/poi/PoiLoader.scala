package kpn.core.poi

trait PoiLoader {
  def load(elementType: String, layer: String, index: String, condition: String): Seq[Poi]
}
