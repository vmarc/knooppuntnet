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
import kpn.server.analyzer.full.InitialFullAnalyzer
import kpn.server.analyzer.load.AnalysisContextLoader
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

@Component
class AnalyzerEngineImpl(
  analyzerReload: Boolean,
  analyzerPoiUpdateEnabled: Boolean,
  analyzerTileUpdateEnabled: Boolean,
  analyzerStatisticsUpdateEnabled: Boolean,
  osmChangeRepository: OsmChangeRepository,
  analysisContextLoader: AnalysisContextLoader,
  initialFullAnalyzer: InitialFullAnalyzer,
  changeSetProcessor: ChangeSetProcessor,
  analysisRepository: AnalysisRepository,
  taskRepository: TaskRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater,
  statisticsUpdater: StatisticsUpdater,
) extends AnalyzerEngine {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def load(replicationId: ReplicationId): Unit = {
    if (analyzerReload) {
      initialFullAnalyzer.analyze(replicationId)
    }
    analysisContextLoader.load()
  }

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
    processPoiUpdates(osmChange)
    processTileUpdates(replicationContext)
    updateStatistics(replicationContext)
    analysisRepository.saveLastUpdated(timestamp)
    val osmChangeTimestamp = osmChange.timestampFrom.map(_.iso).getOrElse("")
    s"$osmChangeTimestamp - ${changeSets.size} changesets, ${replicationContext.changeSetElementCount} elements"
  }

  private def updateStatistics(replicationContext: ReplicationContext): Unit = {
    if (analyzerStatisticsUpdateEnabled && replicationContext.hasChanges) {
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

  private def processPoiUpdates(osmChange: OsmChange): Unit = {
    if (analyzerPoiUpdateEnabled) {
      poiChangeAnalyzer.analyze(osmChange)
    }
  }

  private def processChangeSets(replicationId: ReplicationId, changeSets: Seq[ChangeSet]): ReplicationContext = {
    changeSetProcessor.processChangeSets(replicationId, changeSets)
  }

  private def loadOsmChange(replicationId: ReplicationId) = {
    val osmChange = osmChangeRepository.get(replicationId)
    log.debug(s"osmchange loaded (${osmChange.actions.size} actions)")
    osmChange
  }
}
