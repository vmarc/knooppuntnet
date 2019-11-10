package kpn.core.replicate

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp

trait ReplicationStateRepository {
  def write(replicationId: ReplicationId, state: String): Unit
  def read(replicationId: ReplicationId): Timestamp
}
