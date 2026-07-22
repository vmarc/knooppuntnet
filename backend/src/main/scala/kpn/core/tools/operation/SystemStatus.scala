package kpn.core.tools.operation

import kpn.api.time.TimestampUtil
import kpn.core.replicate.ReplicationStateRepository
import kpn.core.tools.status.StatusRepository

class SystemStatus(
  processReporter: ProcessReporter,
  statusRepository: StatusRepository,
  replicationStateRepository: ReplicationStateRepository
) {

  def status(web: Boolean): String = {
    if (web) {
      processStatus(web)
    }
    else {
      s"""${processStatus(web)}

$toolStatus"""
    }
  }

  private def toolStatus: String = {
    Seq(
      ("replication", statusRepository.replicatorStatus),
      ("update     ", statusRepository.updaterStatus),
      ("analysis   ", statusRepository.analysisStatus1),
      // ("analysis2  ", statusRepository.analysisStatus2),
      // ("analysis3  ", statusRepository.analysisStatus3),
    ).map {
      case (title, Some(replicationId)) =>
        val timestamp = replicationStateRepository.read(replicationId)
        s"$title ${replicationId.name} ${TimestampUtil.toLocal(timestamp).yyyymmddhhmmss}"
      case (title, None) =>
        s"$title ?"
    }.mkString("\n")
  }

  private def processStatus(web: Boolean): String = {
    processStatus(web, processReporter.processes)
  }

  private def processStatus(web: Boolean, lines: List[String]): String = {
    val processes = if (web) {
      Processes.webServerProcesses(lines)
    }
    else {
      Processes.analysisServerProcesses(lines)
    }
    (header +: processLines(processes)).mkString("\n")
  }

  private def processLines(processes: Seq[ProcessInfo]): Seq[String] = {
    processes.map { processInfo =>
      f"${processInfo.name}%-20s ${status(processInfo)}"
    }
  }

  private def status(processInfo: ProcessInfo): String = {
    processInfo.status match {
      case Some(status) => s"OK    ${status.pid}  ${status.start}  ${status.elapsed}"
      case None => "NOK"
    }
  }

  private def header: String = "Name                 Status   PID  Start   Elapsed"
}
