package kpn.server.api.analysis.pages

import kpn.api.common.PoiPage
import kpn.server.analyzer.engine.poi.PoiRef

trait PoiPageBuilder {

  def build(poiRef: PoiRef): Option[PoiPage]

}
