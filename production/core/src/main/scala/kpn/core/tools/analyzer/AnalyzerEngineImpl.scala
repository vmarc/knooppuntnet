package kpn.core.tools.analyzer

import kpn.core.changes.ChangeSetBuilder
import kpn.core.common.TimestampUtil
import kpn.core.engine.changes.ChangeSetContext
import kpn.core.tools.config.Configuration
import kpn.core.util.Log
import kpn.shared.ReplicationId
import kpn.shared.Timestamp

class AnalyzerEngineImpl(config: Configuration) extends AnalyzerEngine {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def load(replicationId: ReplicationId): Unit = {
    val beginOsmChange = config.osmChangeRepository.get(replicationId)
    val timestampAfter = TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, 1)
    config.analysisDataLoader.load(timestampAfter)
  }

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.elapsed {
        val osmChange = config.osmChangeRepository.get(replicationId)
        val timestamp = config.osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
        changeSets.zipWithIndex.foreach { case (changeSet, index) =>
          Log.context(s"${changeSet.id}") {
            val context = ChangeSetContext(replicationId, changeSet)
            config.changeProcessor.process(context)
          }
        }
        config.analysisRepository.saveLastUpdated(timestamp)
        (osmChange.timestampFrom.map(_.iso).getOrElse(""), ())
      }
    }
  }
}
