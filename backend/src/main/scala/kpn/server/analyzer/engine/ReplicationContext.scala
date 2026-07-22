package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId

case class ReplicationContext(
  replicationId: ReplicationId,
  changeSetElementCount: Int = 0,
  hasChanges: Boolean = false,
  tiles: Seq[String] = Seq.empty
)
