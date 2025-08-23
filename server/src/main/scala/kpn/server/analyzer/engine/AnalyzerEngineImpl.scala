package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.core.common.TimestampUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.changes.ChangeProcessorPipeline
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileTask
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.analyzer.full.MainFullAnalyzer
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.TaskRepository
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class AnalyzerEngineImpl(
  analyzerReload: Boolean,
  analyzerPoiUpdateEnabled: Boolean,
  analyzerTileUpdateEnabled: Boolean,
  analyzerStatisticsUpdateEnabled: Boolean,
  osmChangeRepository: OsmChangeRepository,
  analysisDataInitializer: AnalysisDataInitializer,
  mainFullAnalyzer: MainFullAnalyzer,
  changeProcessorPipeline: ChangeProcessorPipeline,
  analysisRepository: AnalysisRepository,
  taskRepository: TaskRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater,
  statisticsUpdater: StatisticsUpdater,
) extends AnalyzerEngine {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def load(replicationId: ReplicationId): Unit = {
    val beginOsmChange = osmChangeRepository.get(replicationId)
    val timestampAfter = if (beginOsmChange.actions.isEmpty) {
      osmChangeRepository.timestamp(replicationId)
    }
    else {
      TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, 1)
    }

    if (analyzerReload) {
      mainFullAnalyzer.analyze(timestampAfter, None)
    }
    analysisDataInitializer.load()
  }

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.infoElapsed {
        val osmChange = osmChangeRepository.get(replicationId)

        log.debug(s"osmchange loaded (${osmChange.actions.size} actions)")

        val timestamp = osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)

        log.debug(s"${changeSets.size} changeSets")

        val replicationContext = processChangeSets(ReplicationContext(replicationId), changeSets)

        log.debug(s"${changeSets.size} changeSets processed")

        if (analyzerPoiUpdateEnabled) {
          poiChangeAnalyzer.analyze(osmChange)
        }

        if (analyzerTileUpdateEnabled) {
          replicationContext.tiles.foreach { tile =>
            taskRepository.add(TileTask.task(tile))
          }
          tileUpdater.update()
          poiTileUpdater.update()
        }

        if (analyzerStatisticsUpdateEnabled && replicationContext.hasChanges) {
          statisticsUpdater.execute()
        }

        analysisRepository.saveLastUpdated(timestamp)

        val osmChangeTimestamp = osmChange.timestampFrom.map(_.iso).getOrElse("")
        val message = s"$osmChangeTimestamp - ${changeSets.size} changesets, ${replicationContext.changeSetElementCount} elements"
        (message, ())
      }
    }
  }

  @tailrec
  private def processChangeSets(replicationContext: ReplicationContext, remainingChangeSets: Seq[ChangeSet]): ReplicationContext = {
    if (remainingChangeSets.isEmpty) {
      replicationContext
    }
    else {
      log.debug(s"processing changeSet ${remainingChangeSets.head.id}")
      val newReplicationContext = processChangeSet(replicationContext, remainingChangeSets.head)
      processChangeSets(newReplicationContext, remainingChangeSets.tail)
    }
  }

  private def processChangeSet(replicationContext: ReplicationContext, changeSet: ChangeSet): ReplicationContext = {
    Log.context(s"${changeSet.id}") {
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        replicationContext.replicationId,
        changeSet,
        elementIds
      )
      val contextAfter = changeProcessorPipeline.process(context)
      val hasChanges = contextAfter.changes.nonEmpty
      replicationContext.copy(
        changeSetElementCount = replicationContext.changeSetElementCount + elementIds.size,
        hasChanges = replicationContext.hasChanges || hasChanges,
        tiles = (replicationContext.tiles ++ contextAfter.impactedTileIds).distinct.sorted,
      )
    }
  }
}
