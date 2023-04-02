package kpn.server.api.analysis.pages

import kpn.api.common.Language
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSetPage

trait ChangeSetPageBuilder {
  def build(
    language: Language,
    changeSetId: Long,
    replicationId: Option[ReplicationId]
  ): Option[ChangeSetPage]
}
