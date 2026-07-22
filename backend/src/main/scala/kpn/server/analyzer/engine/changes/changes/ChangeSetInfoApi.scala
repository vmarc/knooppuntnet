package kpn.server.analyzer.engine.changes.changes

import kpn.api.common.changes.ChangeSetInfo

trait ChangeSetInfoApi {
  def get(changeSetId: Long): Option[ChangeSetInfo]
}
