package kpn.server.analyzer.engine.changes.route.main

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeCreateProcessorTest extends UnitTest with Stubs {
  test("create") {
    // setup
    val processor = new RouteChangeCreateProcessor()

    val context = newChangeSetContext(
    )
    val routeDocAfter = newRouteDoc(
      newRouteSummary(1)
    )

    // execute
    val routeChangeContext = processor.process(context, routeDocAfter, 1)

    // verify
    assertEqual(
      routeChangeContext.get,
      RouteChangeContext(
        routeChange = newRouteChange(),
        impactedNodeIds = Seq.empty,
        impactedNetworkIds = Seq.empty
      )
    )
  }
}
