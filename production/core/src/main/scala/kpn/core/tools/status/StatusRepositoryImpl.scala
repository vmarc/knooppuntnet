package kpn.core.tools.status

import java.io.File

import kpn.core.tools.config.Dirs
import kpn.shared.ReplicationId
import org.apache.commons.io.FileUtils

class StatusRepositoryImpl(dirs: Dirs) extends StatusRepository {

  def replicatorStatus: Option[ReplicationId] = read(dirs.replicationStatus)

  def updaterStatus: Option[ReplicationId] = read(dirs.updateStatus)

  def analysisStatus: Option[ReplicationId] = read(dirs.analysisStatus)

  def changesStatus: Option[ReplicationId] = read(dirs.changesStatus)

  def writeReplicationStatus(replicationId: ReplicationId): Unit = write(dirs.replicationStatus, replicationId)

  def writeUpdateStatus(replicationId: ReplicationId): Unit = write(dirs.updateStatus, replicationId)

  def writeAnalysisStatus(replicationId: ReplicationId): Unit = write(dirs.analysisStatus, replicationId)

  def writeChangesStatus(replicationId: ReplicationId): Unit = write(dirs.changesStatus, replicationId)

  private def read(file: File): Option[ReplicationId] = {
    if (file.exists()) {
      try {
        Some(ReplicationId(FileUtils.readFileToString(file).replaceAll("\n", "").toInt))
      }
      catch {
        case e: NumberFormatException => None
        case e: Throwable => throw e
      }
    }
    else {
      None
    }
  }

  private def write(file: File, replicationId: ReplicationId): Unit = {
    val tempFile = new File(file.getAbsolutePath + ".tmp")
    FileUtils.writeStringToFile(tempFile, s"${replicationId.number}\n")
    tempFile.renameTo(file)
  }
}
