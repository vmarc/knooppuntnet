package kpn.server.api.analysis.pages

import kpn.api.common.PoiPage

trait PoiPageBuilder {

  def build(elementType: String, elementId: Long): Option[PoiPage]

}
