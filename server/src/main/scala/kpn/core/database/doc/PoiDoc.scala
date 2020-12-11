package kpn.core.database.doc

import kpn.api.common.Poi

case class PoiDoc(_id: String, poi: Poi, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
