package kpn.core.poi

import kpn.api.common.Poi
import kpn.core.database.doc.Doc

case class PoiDoc(_id: String, poi: Poi, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
