package kpn.server.analyzer.engine.changes.route.main

import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeDeleteProcessorTest extends UnitTest with Stubs {
  test("delete") {
    // setup
    val processor = new RouteChangeDeleteProcessor()

    val context = newChangeSetContext(
    )
    val routeDoc = newRouteDoc()

    // execute
    val routeChangeContext = processor.process(context, routeDoc)

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
