package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.core.common.TimestampUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.post.OrphanNodeUpdater
import kpn.server.analyzer.engine.analysis.post.OrphanRouteUpdater
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileTask
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.analyzer.full.FullAnalyzer
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
  fullAnalyzer: FullAnalyzer,
  changeProcessor: ChangeProcessor,
  analysisRepository: AnalysisRepository,
  taskRepository: TaskRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater,
  statisticsUpdater: StatisticsUpdater,
  orphanNodeUpdater: OrphanNodeUpdater,
  orphanRouteUpdater: OrphanRouteUpdater,
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
      fullAnalyzer.analyze(timestampAfter)
    }
    analysisDataInitializer.load()
  }

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.infoElapsed {
        val osmChange = osmChangeRepository.get(replicationId)
        val timestamp = osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)

        val elements = osmChange.actions.flatMap(_.elements)
        val nodeCount = elements.count(_.isNode)
        val wayCount = elements.count(_.isWay)
        val relationCount = elements.count(_.isRelation)

        log.info(s"minute diff loaded, ${osmChange.actions.size} actions, ${changeSets.size} changesets, $nodeCount nodes, $wayCount ways, $relationCount relations")

        val replicationContext = processChangeSets(ReplicationContext(replicationId), changeSets)

        log.info(s"${changeSets.size} changeSets processed")

        if (analyzerPoiUpdateEnabled) {
          poiChangeAnalyzer.analyze(osmChange)
        }

        if (analyzerTileUpdateEnabled) {
          replicationContext.tiles.foreach { tile =>
            taskRepository.add(TileTask.task(tile))
          }
          tileUpdater.update(11)
          poiTileUpdater.update()
        }

        if (analyzerStatisticsUpdateEnabled && replicationContext.hasChanges) {
          orphanNodeUpdater.update()
          orphanRouteUpdater.update()
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
      val newReplicationContext = processChangeSet(replicationContext, remainingChangeSets.head)
      processChangeSets(newReplicationContext, remainingChangeSets.tail)
    }
  }

  private def processChangeSet(replicationContext: ReplicationContext, changeSet: ChangeSet): ReplicationContext = {
    log.info(s"processing changeSet ${changeSet.id}")

    Log.context(s"${changeSet.id}") {
      val elementIds = ChangeSetBuilder.elementIdsIn(changeSet)
      val context = ChangeSetContext(
        replicationContext.replicationId,
        changeSet,
        elementIds
      )
      val contextAfter = changeProcessor.process(context)
      val hasChanges = contextAfter.changes.nonEmpty
      val tiles = contextAfter.changes.tiles
      replicationContext.copy(
        changeSetElementCount = replicationContext.changeSetElementCount + elementIds.size,
        hasChanges = replicationContext.hasChanges || hasChanges,
        tiles = (replicationContext.tiles ++ tiles).distinct.sorted,
      )
    }
  }
}
