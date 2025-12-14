package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.changes.ChangeSetProcessor
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileTask
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class AnalyzerEngine(
  analyzerTileUpdateEnabled: Boolean,
  osmChangeRepository: OsmChangeRepository,
  changeSetProcessor: ChangeSetProcessor,
  analysisRepository: AnalysisRepository,
  taskRepository: TaskRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater,
  statisticsUpdater: StatisticsUpdater,
) {

  private val log = Log(classOf[AnalyzerEngine])

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.infoElapsed {
        val message = processOsmChange(replicationId)
        (message, ())
      }
    }
  }

  private def processOsmChange(replicationId: ReplicationId): String = {
    val osmChange = loadOsmChange(replicationId)
    val timestamp = osmChangeRepository.timestamp(replicationId)
    val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
    val replicationContext = processChangeSets(replicationId, changeSets)
    poiChangeAnalyzer.analyze(osmChange)
    processTileUpdates(replicationContext)
    updateStatistics(replicationContext)
    analysisRepository.saveLastUpdated(timestamp)
    val osmChangeTimestamp = osmChange.timestampFrom.map(_.iso).getOrElse("")
    s"$osmChangeTimestamp - ${changeSets.size} changesets, ${replicationContext.changeSetElementCount} elements"
  }

  private def updateStatistics(replicationContext: ReplicationContext): Unit = {
    if (replicationContext.hasChanges) {
      statisticsUpdater.execute()
    }
  }

  private def processTileUpdates(replicationContext: ReplicationContext): Unit = {
    if (analyzerTileUpdateEnabled) {
      replicationContext.tiles.foreach { tile =>
        taskRepository.add(TileTask.task(tile))
      }
      tileUpdater.update()
      poiTileUpdater.update()
    }
  }

  private def processChangeSets(replicationId: ReplicationId, changeSets: Seq[ChangeSet]): ReplicationContext = {
    changeSetProcessor.processChangeSets(replicationId, changeSets)
  }

  private def loadOsmChange(replicationId: ReplicationId): OsmChange = {
    val osmChange = osmChangeRepository.get(replicationId)
    log.debug(s"osmchange loaded (${osmChange.actions.size} actions)")
    osmChange
  }
}
