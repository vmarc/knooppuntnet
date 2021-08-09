package kpn.server.analyzer.engine.changes.orphan.node

import kpn.api.common.data.Node

case class NodeChangeData(
  nodeId: Long,
  before: Option[Node],
  after: Option[Node]
)
