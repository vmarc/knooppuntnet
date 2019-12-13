package kpn.server.analyzer.engine.poi

import kpn.api.common.data.raw.RawElement

trait PoiUpdateProcessor {

  def update(element: RawElement): Unit

}
