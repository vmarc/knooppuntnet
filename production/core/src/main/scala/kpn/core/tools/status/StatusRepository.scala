package kpn.core.tools.status

import kpn.shared.ReplicationId

trait StatusRepository {

  def replicatorStatus: Option[ReplicationId]
  def updaterStatus: Option[ReplicationId]
  def analysisStatus: Option[ReplicationId]
  def changesStatus: Option[ReplicationId]

  def writeReplicationStatus(status: ReplicationId): Unit
  def writeUpdateStatus(status: ReplicationId): Unit
  def writeAnalysisStatus(status: ReplicationId): Unit
  def writeChangesStatus(status: ReplicationId): Unit

}
