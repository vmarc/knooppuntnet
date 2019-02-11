package kpn.core.poi

import kpn.shared.Poi

case class PoiDoc(_id: String, poi: Poi, _rev: Option[String] = None)
