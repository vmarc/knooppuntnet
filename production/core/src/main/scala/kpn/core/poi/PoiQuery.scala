package kpn.core.poi

import kpn.core.overpass.OverpassQuery

case class PoiQuery(elementType: String, layer: String, index: String, condition: String) extends OverpassQuery {

  def name: String = s"poi-$elementType-$layer-$index"

  def string: String = {
    s"$elementType$condition(48.98, 0.65, 54.27, 13.14);out center;"
  }

}
