package kpn.core.database.doc

import kpn.server.analyzer.engine.changes.data.BlackList

case class BlackListDoc(_id: String, blackList: BlackList, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
