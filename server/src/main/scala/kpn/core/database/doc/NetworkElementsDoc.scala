package kpn.core.database.doc

import kpn.server.analyzer.engine.changes.changes.NetworkElements

case class NetworkElementsDoc(_id: String, networkElements: NetworkElements, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}

