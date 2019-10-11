package kpn.server.api.analysis.pages

import kpn.shared.PoiPage

trait PoiPageBuilder {

  def build(elementType: String, elementId: Long): Option[PoiPage]

}
