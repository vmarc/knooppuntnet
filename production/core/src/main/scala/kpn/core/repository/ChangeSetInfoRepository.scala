package kpn.core.repository

import kpn.shared.changes.ChangeSetInfo

class ChangeSetInfoRepository {

  def save(changeSetInfo: ChangeSetInfo): Unit = {}

  def get(changeSetId: Long): Option[ChangeSetInfo] = None

  def all(changeSetIds: Seq[Long], stale: Boolean = true): Seq[ChangeSetInfo] = Seq.empty

  def exists(changeSetId: Long): Boolean = false

  def delete(changeSetId: Long): Unit = {}
}
