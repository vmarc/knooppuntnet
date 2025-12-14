package kpn.server.analyzer.engine

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.ChangeSet
import kpn.api.custom.Change
import kpn.core.common.TimestampUtil
import kpn.core.test.TestObjects.newNodeWithName
import kpn.core.test.TestObjects.newOsmChange
import kpn.core.test.Timestamps
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.post.StatisticsUpdater
import kpn.server.analyzer.engine.changes.ChangeSetProcessor
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.poi.PoiChangeAnalyzer
import kpn.server.analyzer.engine.poi.PoiTileUpdater
import kpn.server.analyzer.engine.tile.TileUpdater
import kpn.server.repository.AnalysisRepository
import kpn.server.repository.TaskRepository
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class AnalyzerEngineTest extends UnitTest with Stubs {

  test("process changes in OsmChange") {
    val setup = new Setup()

    (setup.osmChangeRepository.get _).returns {
      case ReplicationId(0, 0, 1) =>
        OsmChange(
          Seq(
            Change(
              ChangeAction.Create,
              Seq(
                newNodeWithName(1001, "01").toRaw
              )
            )
          )
        )
      case _ => throw new IllegalArgumentException()
    }

    (setup.changeSetProcessor.processChangeSets _).returnsWith(
      ReplicationContext(ReplicationId(0, 0, 1))
    )

    // execute
    setup.engine.process(ReplicationId(0, 0, 1))

    // verify
    val changeSets = (setup.changeSetProcessor.processChangeSets _).calls.map(_._2).head
    changeSets should equal(
      Seq(
        ChangeSet(
          0,
          timestamp = Timestamps.default,
          timestampFrom = Timestamps.default,
          timestampUntil = Timestamps.default,
          timestampBefore = TimestampUtil.relativeSeconds(Timestamps.default, -1),
          timestampAfter = TimestampUtil.relativeSeconds(Timestamps.default, 1),
          Seq(
            Change(
              ChangeAction.Create,
              Seq(newNodeWithName(1001, "01").toRaw)
            )
          )
        )
      )
    )

    (setup.poiChangeAnalyzer.analyze _).times should equal(1)
    (setup.tileUpdater.update _).times should equal(1)
    (setup.poiTileUpdater.update _).times should equal(1)
    (setup.analysisRepository.saveLastUpdated _).calls should equal(Seq(Timestamps.default))
  }

  test("statistics are updated when there are changes in the replication context after change processing") {
    val setup = new Setup()

    (setup.osmChangeRepository.get _).returnsWith(newOsmChange())

    (setup.changeSetProcessor.processChangeSets _).returnsWith(
      ReplicationContext(
        ReplicationId(0, 0, 1),
        hasChanges = true,
      )
    )

    // execute
    setup.engine.process(ReplicationId(0, 0, 1))

    // verify
    (setup.statisticsUpdater.execute _).times should equal(1)
  }

  test("statistics are not updated when there are no changes") {
    val setup = new Setup()

    (setup.osmChangeRepository.get _).returnsWith(newOsmChange())

    (setup.changeSetProcessor.processChangeSets _).returnsWith(
      ReplicationContext(
        ReplicationId(0, 0, 1)
      )
    )

    // execute
    setup.engine.process(ReplicationId(0, 0, 1))

    // verify
    (setup.statisticsUpdater.execute _).times should equal(0)
  }

  test("tile tasks are created there are tiles in the replication context after change processing") {
    val setup = new Setup()

    (setup.osmChangeRepository.get _).returnsWith(newOsmChange())

    (setup.changeSetProcessor.processChangeSets _).returnsWith(
      ReplicationContext(
        ReplicationId(0, 0, 1),
        tiles = Seq("tile-1"),
      )
    )

    // execute
    setup.engine.process(ReplicationId(0, 0, 1))

    // verify
    (setup.taskRepository.add _).calls should equal(Seq("tile-task:tile-1"))
  }

  private class Setup(analyzerTileUpdateEnabled: Boolean = true) {
    val osmChangeRepository: Stub[OsmChangeRepository] = stub[OsmChangeRepository]
    val changeSetProcessor: Stub[ChangeSetProcessor] = stub[ChangeSetProcessor]
    val analysisRepository: Stub[AnalysisRepository] = stub[AnalysisRepository]
    val taskRepository: Stub[TaskRepository] = stub[TaskRepository]
    val tileUpdater: Stub[TileUpdater] = stub[TileUpdater]
    val poiChangeAnalyzer: Stub[PoiChangeAnalyzer] = stub[PoiChangeAnalyzer]
    val poiTileUpdater: Stub[PoiTileUpdater] = stub[PoiTileUpdater]
    val statisticsUpdater: Stub[StatisticsUpdater] = stub[StatisticsUpdater]

    (osmChangeRepository.timestamp _).returns {
      case ReplicationId(0, 0, 1) => Timestamps.default
      case _ => throw new IllegalArgumentException()
    }

    (poiChangeAnalyzer.analyze _).returnsWith(())
    (taskRepository.add _).returnsWith(())
    (tileUpdater.update _).returnsWith(())
    (poiTileUpdater.update _).returnsWith(())
    (statisticsUpdater.execute _).returnsWith(())
    (analysisRepository.saveLastUpdated _).returnsWith(())

    val engine = new AnalyzerEngine(
      analyzerTileUpdateEnabled,
      osmChangeRepository,
      changeSetProcessor,
      analysisRepository,
      taskRepository,
      tileUpdater,
      poiChangeAnalyzer,
      poiTileUpdater,
      statisticsUpdater
    )
  }
}
