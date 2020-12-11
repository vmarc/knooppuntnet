package kpn.core.database.doc

import kpn.core.action.ReplicationAction

case class ReplicationActionDoc(_id: String, replication: ReplicationAction, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
