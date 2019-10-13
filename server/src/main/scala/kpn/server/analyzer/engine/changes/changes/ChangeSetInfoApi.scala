package kpn.server.analyzer.engine.changes.changes

import kpn.shared.changes.ChangeSetInfo

trait ChangeSetInfoApi {
  def get(changeSetId: Long): Option[ChangeSetInfo]
}
