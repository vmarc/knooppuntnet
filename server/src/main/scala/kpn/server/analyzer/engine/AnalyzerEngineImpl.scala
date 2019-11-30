package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.core.common.TimestampUtil
import kpn.core.util.Log
import kpn.server.analyzer.engine.changes.ChangeProcessor
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.ChangeSetBuilder
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.analyzer.load.AnalysisDataLoader
import kpn.server.repository.AnalysisRepository
import org.springframework.stereotype.Component

@Component
class AnalyzerEngineImpl(
  osmChangeRepository: OsmChangeRepository,
  analysisDataLoader: AnalysisDataLoader,
  changeProcessor: ChangeProcessor,
  analysisRepository: AnalysisRepository,
  tileUpdater: TileUpdater,
  poiChangeAnalyzer: PoiChangeAnalyzer,
  poiTileUpdater: PoiTileUpdater
) extends AnalyzerEngine {

  private val log = Log(classOf[AnalyzerEngineImpl])

  def load(replicationId: ReplicationId): Unit = {
    val beginOsmChange = osmChangeRepository.get(replicationId)
    val timestampAfter = TimestampUtil.relativeSeconds(beginOsmChange.timestampUntil.get, 1)
    analysisDataLoader.load(timestampAfter)
  }

  def process(replicationId: ReplicationId): Unit = {
    Log.context(s"${replicationId.name}") {
      log.debug("Start")
      log.elapsed {
        val osmChange = osmChangeRepository.get(replicationId)
        val timestamp = osmChangeRepository.timestamp(replicationId)
        val changeSets = ChangeSetBuilder.from(timestamp, osmChange)
        changeSets.foreach { changeSet =>
          Log.context(s"${changeSet.id}") {
            val context = ChangeSetContext(replicationId, changeSet)
            changeProcessor.process(context)
          }
        }

        tileUpdater.update(12)
        // poiChangeAnalyzer.analyze(osmChange)
        // poiTileUpdater.update()

        analysisRepository.saveLastUpdated(timestamp)
        (osmChange.timestampFrom.map(_.iso).getOrElse(""), ())
      }
    }
  }
}
