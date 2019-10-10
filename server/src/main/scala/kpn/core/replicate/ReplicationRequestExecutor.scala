package kpn.core.replicate

import kpn.shared.ReplicationId

trait ReplicationRequestExecutor {

  def requestChangesFile(replicationId: ReplicationId): Option[String]

  def requestStateFile(replicationId: ReplicationId): Option[String]

}
