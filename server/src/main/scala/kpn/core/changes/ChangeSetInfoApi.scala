package kpn.core.changes

import kpn.shared.changes.ChangeSetInfo

trait ChangeSetInfoApi {
  def get(changeSetId: Long): Option[ChangeSetInfo]
}
