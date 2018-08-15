package kpn.core.replicate

import kpn.shared.ReplicationId
import kpn.shared.Timestamp

trait ReplicationStateRepository {
  def write(replicationId: ReplicationId, state: String): Unit
  def read(replicationId: ReplicationId): Timestamp
}
