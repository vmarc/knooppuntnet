package kpn.server.analyzer

import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.util.Log
import kpn.server.analyzer.engine.AnalyzerEngine
import kpn.server.analyzer.engine.CouchIndexer
import kpn.shared.ReplicationId
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class AnalyzerImpl(
  statusRepository: StatusRepository,
  analysisDatabaseIndexer: CouchIndexer,
  engine: AnalyzerEngine,
  dirs: Dirs
) extends Analyzer {

  private val log = Log(classOf[AnalyzerImpl])

  def load(): Unit = {
    statusRepository.analysisStatus2 match {
      case None => log.error("Could not start: failed to read analysis status " + dirs.analysisStatus2)
      case Some(replicationId) =>
        analysisDatabaseIndexer.index()
        engine.load(replicationId)
    }
  }

  def process(): Unit = {
    statusRepository.analysisStatus2 match {
      case None => log.error("Could not start: failed to read analysis status")
      case Some(replicationId) => processLoop(replicationId)
    }
  }

  @tailrec
  private def processLoop(previousReplicationId: ReplicationId): Unit = {

    val replicationId = previousReplicationId.next
    val updaterReplicationId = readUpdaterReplicationId()

    if (replicationId.number <= updaterReplicationId.number) {
      //      if (oper.isActive) {
      engine.process(replicationId)
      statusRepository.writeAnalysisStatus2(replicationId)
      //if (oper.isActive) {
      processLoop(replicationId)
      //}
      //      }
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
}
