package kpn.server.analyzer

import kpn.core.util.UnitTest
import kpn.server.config.Mailer
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class AnalyzerJobTest extends UnitTest with Stubs {

  test("job calls load() once, and then keeps calling process()") {

    // setup
    val setup = new Setup()

    // execute
    (1 to 4).foreach(_ => setup.job.analyze())

    // verify
    (setup.analyzer.load _).times should equal(1)
    (setup.analyzer.process _).times should equal(4)
    (setup.mailer.send _).times should equal(0)
  }

  test("abort during initial load") {

    // setup
    val setup = new Setup(abortLoad = true)

    // execute
    (1 to 4).foreach(_ => setup.job.analyze())

    // verify
    (setup.analyzer.load _).times should equal(1)
    (setup.analyzer.process _).times should equal(0)
    (setup.mailer.send _).calls.map(_._1) should equal(Seq("application-name analyzer aborted during initial load"))
  }

  test("abort during second call to process()") {

    // setup
    val setup = new Setup(abortProcess = true)

    // execute
    (1 to 4).foreach(_ => setup.job.analyze())

    // verify
    (setup.analyzer.load _).times should equal(1)
    (setup.analyzer.process _).times should equal(2)
    (setup.mailer.send _).calls.map(_._1) should equal(Seq("application-name analyzer aborted"))
  }

  private class Setup(abortLoad: Boolean = false, abortProcess: Boolean = false) {

    val applicationName = "application-name"

    val analyzer: Stub[Analyzer] = stub[Analyzer]
    (analyzer.load _).returnsOnCall { _ =>
      if (abortLoad) {
        throw new IllegalStateException()
      }
    }
    (analyzer.process _).returnsOnCall { (callNumber: Int) =>
      if (abortProcess && callNumber == 2) {
        throw new IllegalStateException()
      }
    }

    val mailer: Stub[Mailer] = stub[Mailer]
    (mailer.send _).returnsWith(())

    val job = new AnalyzerJob(applicationName, analyzer, mailer)
  }
}
