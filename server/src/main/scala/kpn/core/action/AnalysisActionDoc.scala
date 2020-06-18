package kpn.core.action

import kpn.core.database.doc.Doc

case class AnalysisActionDoc(_id: String, analysis: AnalysisAction, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
