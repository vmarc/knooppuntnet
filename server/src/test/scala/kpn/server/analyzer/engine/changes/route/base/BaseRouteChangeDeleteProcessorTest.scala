package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.ChangeSetContext

class BaseRouteChangeDeleteProcessorTest extends UnitTest with SharedTestObjects {

  private class Setup {
    val baseRouteDeleter: BaseRouteChangeDeleterImpl = stub[BaseRouteChangeDeleterImpl]
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

    // execute
    val updatedChangeSetContext = setup.process()

    // verify
    (setup.baseRouteDeleter.delete _).verify(
      where { (context: ChangeSetContext, routeId: Long) =>
        routeId == 11
      }
    ).once()
  }
}
