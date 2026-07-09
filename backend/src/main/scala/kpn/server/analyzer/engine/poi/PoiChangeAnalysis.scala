package kpn.server.analyzer.engine.poi

import kpn.api.common.data.raw.RawElement
import kpn.api.common.LatLon
import kpn.core.poi.PoiDefinition

case class PoiChangeAnalysis(
  element: RawElement,
  poiDefinitions: Seq[PoiDefinition],
  center: Option[LatLon] = None
)
