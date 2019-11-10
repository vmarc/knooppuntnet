package kpn.core.database.doc

import kpn.api.common.network.NetworkInfo

case class NetworkDoc(_id: String, network: NetworkInfo, _rev: Option[String] = None) extends Doc
