package kpn.server.analyzer.engine.changes.route.main

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeUpdateProcessorTest extends UnitTest with Stubs {
  test("update") {
    // setup
    val processor = new RouteChangeUpdateProcessor()

    val context = newChangeSetContext(
    )
    val routeDocBefore = newRouteDoc(
      newRouteSummary(1)
    )
    val routeDocAfter = newRouteDoc(
      newRouteSummary(1)
    )

    // execute
    val routeChangeContext = processor.process(context, routeDocBefore, routeDocAfter, 1)

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
