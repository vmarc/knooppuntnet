package kpn.server.api.analysis.pages

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetPage

trait ChangeSetPageBuilder {
  def build(user: Option[String], changeSetId: Long, replicationId: Option[ReplicationId]): Option[ChangeSetPage]
}
