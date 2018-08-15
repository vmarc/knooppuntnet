package kpn.core.facade.pages

import kpn.shared.ReplicationId
import kpn.shared.changes.ChangeSetPage

trait ChangeSetPageBuilder {
  def build(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): Option[ChangeSetPage]
}
