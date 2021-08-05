package kpn.server.analyzer.engine.changes.network

import kpn.core.mongo.doc.NetworkDoc

case class NetworkChangeData(
  networkId: Long,
  changeAction: Int,
  before: Option[NetworkDoc],
  after: Option[NetworkDoc]
)
