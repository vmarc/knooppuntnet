package kpn.core.poi

import kpn.core.overpass.OverpassQuery

case class PoiQuery(elementType: String, layer: String, bbox: String, condition: String) extends OverpassQuery {

  def name: String = s"poi-$elementType-$layer"

  def string: String = {
    s"$elementType$condition$bbox;out center;"
  }

}
