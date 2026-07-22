package kpn.core.replicate

import kpn.api.common.ReplicationId
import kpn.api.custom.Timestamp
import kpn.api.time.TimestampUtil
import org.apache.commons.io.FileUtils

import java.io.File

class ReplicationStateRepository(replicateDir: File) {

  def write(replicationId: ReplicationId, state: String): Unit = {
    stateDir(replicationId).mkdirs()
    FileUtils.writeStringToFile(stateFile(replicationId), state, "UTF-8")
  }

  def read(replicationId: ReplicationId): Timestamp = {
    val state = FileUtils.readFileToString(stateFile(replicationId), "UTF-8")
    val lines = state.split("\n")
    lines.find(_.startsWith("timestamp=")) match {
      case Some(line) =>
        val timestamp = line.drop("timestamp=".length).replaceAll("\\\\:", ":")
        TimestampUtil.parseIso(timestamp)
      case None => throw new RuntimeException(
        s"""Timestamp line not found in:
$state""")
    }
  }

  private def stateFile(replicationId: ReplicationId): File = new File(replicateDir, s"${replicationId.name}.state.txt")

  private def stateDir(replicationId: ReplicationId): File = {
    new File(replicateDir, f"${replicationId.level1}%03d/${replicationId.level2}%03d")
  }
}
