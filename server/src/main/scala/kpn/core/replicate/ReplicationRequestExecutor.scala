package kpn.core.replicate

import kpn.api.common.ReplicationId

trait ReplicationRequestExecutor {

  def requestChangesFile(replicationId: ReplicationId): Option[String]

  def requestStateFile(replicationId: ReplicationId): Option[String]

}
