package kpn.server.analyzer

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalyzerEngine
import org.springframework.stereotype.Component

import java.io.File
import javax.annotation.PreDestroy
import scala.annotation.tailrec

@Component
class AnalyzerImpl(
  analyzerStatusFile: String,
  statusRepository: StatusRepository,
  engine: AnalyzerEngine,
  dirs: Dirs
) extends Analyzer {

  private val log = Log(classOf[AnalyzerImpl])

  var shutdownRequestReceived = false
  var active = false

  @PreDestroy
  def onExit(): Unit = {
    val SLEEP_SHUTDOWN_POLL_INTERVAL = 250
    log.info("Shutting down")
    val seconds = 30
    val end = System.currentTimeMillis() + (seconds * 1000)
    while (active && System.currentTimeMillis() < end) {
      Thread.sleep(SLEEP_SHUTDOWN_POLL_INTERVAL)
    }
    log.info("Done")
  }

  def load(): Unit = {
    readStatus() match {
      case None => log.error(s"Could not start: failed to read analysis status $analyzerStatusFile")
      case Some(replicationId) =>
        engine.load(replicationId)
    }
  }

  def process(): Unit = {
    try {
      active = true
      readStatus() match {
        case None => log.error(s"Could not start: failed to read analysis status $analyzerStatusFile")
        case Some(replicationId) => processLoop(replicationId)
      }
    }
    finally {
      active = false
    }
  }

  @tailrec
  private def processLoop(previousReplicationId: ReplicationId): Unit = {
    if (shutdownRequestReceived) {
      return
    }
    val replicationId = previousReplicationId.next
    val updaterReplicationId = readUpdaterReplicationId()

    if (replicationId.number <= updaterReplicationId.number) {
      if (shutdownRequestReceived) {
        return
      }
      engine.process(replicationId)
      writeStatus(replicationId)
      processLoop(replicationId)
    }
  }

  private def readUpdaterReplicationId(): ReplicationId = {
    statusRepository.updaterStatus match {
      case Some(updaterReplicationId) => updaterReplicationId
      case None =>
        val message = "Could not read " + dirs.updateStatus.getAbsolutePath
        log.error(message)
        throw new RuntimeException(message)
    }
  }

  private def readStatus(): Option[ReplicationId] = {
    statusRepository.read(new File(analyzerStatusFile))
  }

  private def writeStatus(replicationId: ReplicationId): Unit = {
    statusRepository.write(new File(analyzerStatusFile), replicationId)
  }
}
