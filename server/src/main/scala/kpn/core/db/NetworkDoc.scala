package kpn.core.db

import kpn.shared.network.NetworkInfo

case class NetworkDoc(_id: String, network: NetworkInfo, _rev: Option[String] = None) extends Doc
