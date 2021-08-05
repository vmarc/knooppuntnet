package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.core.common.TimestampUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.node.NodeRouteUpdater
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.analyzer.full.FullAnalyzer
import kpn.server.analyzer.load.AnalysisDataInitializer
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

@Component
class AnalyzerEngineImpl(
  analysisContext: AnalysisContext,
  analyzerHistory: Boolean,
  analyzerTileUpdateEnabled: Boolean,
  osmChangeRepository: OsmChangeRepository,
  analysisDataInitializer: AnalysisDataInitializer,
  fullAnalyzer: FullAnalyzer,
  changeProcessor: ChangeProcessor,
  analysisRepository: AnalysisRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater,
  nodeRouteUpdater: NodeRouteUpdater,
  analyzerReload: Boolean
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
    else {
      analysisDataInitializer.load()
    }
  }

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.infoElapsed {
        val osmChange = osmChangeRepository.get(replicationId)
        val timestamp = osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
        changeSets.foreach { changeSet =>
          Log.context(s"${changeSet.id}") {
            val context = ChangeSetContext(replicationId, changeSet)
            changeProcessor.process(context)
          }
        }

        if (!analyzerHistory) {
          nodeRouteUpdater.update()
        }

        if (analyzerTileUpdateEnabled) {
          tileUpdater.update(11)
          poiChangeAnalyzer.analyze(osmChange)
          poiTileUpdater.update()
        }

        analysisRepository.saveLastUpdated(timestamp)
        (osmChange.timestampFrom.map(_.iso).getOrElse(""), ())
      }
    }
  }
}
