package kpn.server.analyzer.engine.changes.route.base

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext
import org.scalamock.stubs.Stub
import org.scalamock.stubs.Stubs

class BaseRouteChangeDeleteProcessorTest extends UnitTest with Stubs {

  private class Setup {
    val baseRouteDeleter: Stub[BaseRouteChangeDeleterImpl] = stub[BaseRouteChangeDeleterImpl]
    val processor = new BaseRouteChangeDeleteProcessor(
      baseRouteDeleter
    )

    def process(): ChangeSetContext = {
      processor.process(newChangeSetContext(), 11)
    }
  }

  test("delete route") {

    // setup
    val setup = new Setup()
    (setup.baseRouteDeleter.delete _).returns {
      case (changeSetContext: ChangeSetContext, routeId: Long) => changeSetContext
      case _ => throw new IllegalArgumentException()
    }

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    (setup.baseRouteDeleter.delete _).calls.map(_._2) should equal(Seq(11))
  }
}
