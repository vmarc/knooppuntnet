package kpn.server.analyzer

import kpn.api.common.ReplicationId
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.AnalyzerEngine
import kpn.server.analyzer.full.InitialFullAnalyzer
import kpn.server.analyzer.load.AnalysisContextLoader
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class AnalyzerTest extends UnitTest with Stubs {

  test("load") {

    // setup
    val setup = new Setup()
    val lastProcessedReplicationId = ReplicationId(0, 0, 1)
    (setup.statusRepository.read _).returnsWith(Some(lastProcessedReplicationId))

    // execute
    setup.analyzer.load()

    // verify
    (setup.initialFullAnalyzer.analyze _).calls should equal(Seq(lastProcessedReplicationId))
    (setup.analysisContextLoader.load _).times should equal(1)
    (setup.engine.process _).times should equal(0)
    (setup.statusRepository.write _).times should equal(0)
  }

  test("no load when status cannot be read") {

    // setup
    val setup = new Setup()
    (setup.statusRepository.read _).returnsWith(None)

    // execute
    setup.analyzer.load()

    // verify
    (setup.initialFullAnalyzer.analyze _).times should equal(0)
    (setup.analysisContextLoader.load _).times should equal(0)
    (setup.engine.process _).times should equal(0)
    (setup.statusRepository.write _).times should equal(0)
  }

  test("process next replication file") {

    // setup
    val lastProcessedReplicationId = Some(ReplicationId(0, 0, 1))
    val lastUpdateReplicationId = Some(ReplicationId(0, 0, 3))
    val expectedReplicationIds = Seq(ReplicationId(0, 0, 2), ReplicationId(0, 0, 3))

    val setup = new Setup()
    (setup.statusRepository.read _).returnsWith(lastProcessedReplicationId)
    (() => setup.statusRepository.updaterStatus).returnsWith(lastUpdateReplicationId)

    // execute
    setup.analyzer.process()

    // verify
    (setup.engine.process _).calls should equal(expectedReplicationIds)
    (setup.statusRepository.write _).calls.map(_._2) should equal(expectedReplicationIds)
    (setup.statusRepository.write _).calls.map(_._1.getName) should equal(Seq("filename", "filename"))
  }

  test("nothing to process (last updated is last processed)") {

    // setup
    val lastProcessedReplicationId = Some(ReplicationId(0, 0, 1))
    val lastUpdateReplicationId = Some(ReplicationId(0, 0, 1))

    val setup = new Setup()
    (setup.statusRepository.read _).returnsWith(lastProcessedReplicationId)
    (() => setup.statusRepository.updaterStatus).returnsWith(lastUpdateReplicationId)

    // execute
    setup.analyzer.process()

    // verify
    (setup.engine.process _).calls should equal(Seq.empty)
    (setup.statusRepository.write _).calls.map(_._2) should equal(Seq.empty)
    (setup.statusRepository.write _).calls.map(_._1.getName) should equal(Seq.empty)
  }

  private class Setup {
    private val dirs = stub[Dirs]
    private val analyzerStatusFile = "filename"

    val statusRepository: Stub[StatusRepository] = stub[StatusRepository]
    (statusRepository.write _).returnsWith(())

    val initialFullAnalyzer: Stub[InitialFullAnalyzer] = stub[InitialFullAnalyzer]
    (initialFullAnalyzer.analyze _).returnsWith(())

    val analysisContextLoader: Stub[AnalysisContextLoader] = stub[AnalysisContextLoader]
    (analysisContextLoader.load _).returnsWith(())

    val engine: Stub[AnalyzerEngine] = stub[AnalyzerEngine]
    (engine.process _).returnsWith(())

    val analyzer = new Analyzer(
      analyzerStatusFile,
      statusRepository,
      initialFullAnalyzer,
      analysisContextLoader,
      engine,
      dirs
    )
  }
}
