package kpn.core.repository

import kpn.shared.ReplicationId
import kpn.shared.changes.Review

trait ReviewRepository {

  def save(changeSetId: Long, replicationId: ReplicationId, review: Review): Unit

  def get(changeSetId: Long, replicationId: ReplicationId): Seq[Review]

}
