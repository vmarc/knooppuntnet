package kpn.core.action

import kpn.core.database.doc.Doc

case class AnalysisActionDoc(_id: String, analysis: AnalysisAction, _rev: Option[String] = None) extends Doc
