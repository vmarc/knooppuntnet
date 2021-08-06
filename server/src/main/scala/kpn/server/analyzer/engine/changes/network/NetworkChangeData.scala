package kpn.server.analyzer.engine.changes.network

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.core.mongo.doc.NetworkDoc

case class NetworkChangeData(
  networkId: Long,
  changeAction: ChangeAction,
  before: Option[NetworkDoc],
  after: Option[NetworkDoc]
)
