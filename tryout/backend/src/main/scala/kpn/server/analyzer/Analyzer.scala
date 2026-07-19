package kpn.server.analyzer

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalyzerEngine
import kpn.server.analyzer.full.InitialFullAnalyzer
import kpn.server.analyzer.load.AnalysisContextLoader
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import java.io.File
import javax.annotation.PreDestroy
import scala.annotation.tailrec

@Component
@Profile(Array("analysis"))
class Analyzer(
  analyzerStatusFile: String,
  statusRepository: StatusRepository,
  initialFullAnalyzer: InitialFullAnalyzer,
  analysisContextLoader: AnalysisContextLoader,
  engine: AnalyzerEngine,
  dirs: Dirs
) {

  private val log = Log(classOf[Analyzer])

  private var shutdownRequestReceived = false
  var active = false

  @PreDestroy
  def onExit(): Unit = {
    val SLEEP_SHUTDOWN_POLL_INTERVAL = 250 // seconds
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
        initialFullAnalyzer.analyze(replicationId)
        analysisContextLoader.load()
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
        val message = s"Could not read ${dirs.updateStatus.getAbsolutePath}"
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
