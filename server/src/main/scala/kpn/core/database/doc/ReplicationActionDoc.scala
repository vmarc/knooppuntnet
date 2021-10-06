package kpn.core.database.doc

import kpn.api.base.WithStringId
import kpn.core.action.ReplicationAction

case class ReplicationActionDoc(_id: String, replication: ReplicationAction, _rev: Option[String] = None) extends CouchDoc with WithStringId {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
