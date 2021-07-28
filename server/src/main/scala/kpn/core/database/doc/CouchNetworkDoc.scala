package kpn.core.database.doc

import kpn.api.common.network.NetworkInfo

case class CouchNetworkDoc(_id: String, network: NetworkInfo, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
