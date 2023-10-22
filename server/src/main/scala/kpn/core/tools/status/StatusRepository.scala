package kpn.core.tools.status

import kpn.api.common.ReplicationId

import java.io.File

trait StatusRepository {

  def replicatorStatus: Option[ReplicationId]

  def updaterStatus: Option[ReplicationId]

  def analysisStatus1: Option[ReplicationId]

  def analysisStatus2: Option[ReplicationId]

  def analysisStatus3: Option[ReplicationId]

  def writeReplicationStatus(status: ReplicationId): Unit

  def writeUpdateStatus(status: ReplicationId): Unit

  def writeAnalysisStatus1(status: ReplicationId): Unit

  def writeAnalysisStatus2(status: ReplicationId): Unit

  def writeAnalysisStatus3(status: ReplicationId): Unit

  def read(file: File): Option[ReplicationId]

  def write(file: File, replicationId: ReplicationId): Unit

}
