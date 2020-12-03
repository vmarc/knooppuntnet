package kpn.core.tools.operation

import kpn.core.common.TimestampUtil
import kpn.core.replicate.ReplicationStateRepository
import kpn.core.tools.status.StatusRepository

class SystemStatus(
  processReporter: ProcessReporter,
  statusRepository: StatusRepository,
  replicationStateRepository: ReplicationStateRepository
) {

  def status(): String = {
    processStatus() + "\n\n" + toolStatus
  }

  private def toolStatus: String = {
    Seq(
      ("replication", statusRepository.replicatorStatus),
      ("update     ", statusRepository.updaterStatus),
      ("changes    ", statusRepository.changesStatus),
      ("analysis1  ", statusRepository.analysisStatus1),
      ("analysis2  ", statusRepository.analysisStatus2),
      ("analysis3  ", statusRepository.analysisStatus3)
    ).map {
      case (title, Some(replicationId)) =>
        val timestamp = replicationStateRepository.read(replicationId)
        title + " " + replicationId.name + " " + TimestampUtil.toLocal(timestamp).yyyymmddhhmmss
      case (title, None) =>
        title + " ?"
    }.mkString("\n")
  }

  private def processStatus(): String = {
    processStatus(processReporter.processes)
  }

  private def processStatus(lines: List[String]): String = {
    val processes = Processes(lines)
    (header +: processLines(processes)).mkString("\n")
  }

  private def processLines(processes: Processes): Seq[String] = {
    processes.all.map { processInfo =>
      "%16s ".format(processInfo.name) + status(processInfo)
    }
  }

  private def status(processInfo: ProcessInfo): String = {
    processInfo.status match {
      case Some(status) => "OK    " + status.pid + "  " + status.start + "  " + status.elapsed
      case None => "NOK"
    }
  }

  private def header: String = "            Name Status   PID  Start   Elapsed"
}
