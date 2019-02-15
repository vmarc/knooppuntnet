package kpn.core.facade.pages

import kpn.shared.PoiPage

trait PoiPageBuilder {

  def build(elementType: String, elementId: Long): Option[PoiPage]

}
