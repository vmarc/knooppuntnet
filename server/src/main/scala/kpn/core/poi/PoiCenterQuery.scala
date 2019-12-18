package kpn.core.poi

import kpn.core.overpass.OverpassQuery
import kpn.server.analyzer.engine.poi.PoiRef

case class PoiCenterQuery(poiRef: PoiRef) extends OverpassQuery {

  def name: String = s"poi-center-${poiRef.elementType}-${poiRef.elementId}"

  def string: String = {
    s"${poiRef.elementType}(${poiRef.elementId});out center;"
  }

}
