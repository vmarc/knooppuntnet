package kpn.core.tools.status

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import org.apache.commons.io.FileUtils

import java.io.File

class StatusRepositoryImpl(dirs: Dirs) extends StatusRepository {

  def replicatorStatus: Option[ReplicationId] = read(dirs.replicationStatus)

  def updaterStatus: Option[ReplicationId] = read(dirs.updateStatus)

  def analysisStatus1: Option[ReplicationId] = read(dirs.analysisStatus1)

  def analysisStatus2: Option[ReplicationId] = read(dirs.analysisStatus2)

  def analysisStatus3: Option[ReplicationId] = read(dirs.analysisStatus3)

  def writeReplicationStatus(replicationId: ReplicationId): Unit = write(dirs.replicationStatus, replicationId)

  def writeUpdateStatus(replicationId: ReplicationId): Unit = write(dirs.updateStatus, replicationId)

  def writeAnalysisStatus1(replicationId: ReplicationId): Unit = write(dirs.analysisStatus1, replicationId)

  def writeAnalysisStatus2(replicationId: ReplicationId): Unit = write(dirs.analysisStatus2, replicationId)

  def writeAnalysisStatus3(replicationId: ReplicationId): Unit = write(dirs.analysisStatus3, replicationId)

  override def read(file: File): Option[ReplicationId] = {
    if (file.exists()) {
      try {
        Some(ReplicationId(FileUtils.readFileToString(file, "UTF-8").replaceAll("\n", "").toInt))
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

  override def write(file: File, replicationId: ReplicationId): Unit = {
    val tempFile = new File(file.getAbsolutePath + ".tmp")
    FileUtils.writeStringToFile(tempFile, s"${replicationId.number}\n", "UTF-8")
    tempFile.renameTo(file)
  }
}
