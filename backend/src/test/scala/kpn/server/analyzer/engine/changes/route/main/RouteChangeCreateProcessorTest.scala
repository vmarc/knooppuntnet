package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.RouteType
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeCreateProcessorTest extends UnitTest with Stubs {
  test("create") {
    // setup
    val processor = new RouteChangeCreateProcessor()
    val context = newChangeSetContext()
    val routeDocAfter = newRouteDoc()

    // execute
    val routeChangeContext = processor.process(context, routeDocAfter, 1)

    // verify
    assertEqual(
      routeChangeContext.get,
      RouteChangeContext(
        routeChange = newRouteChange(
          key = newChangeKey(elementId = 1),
          after = Some(
            newRouteData(
              relationId = 1,
              routeTypes = Seq(RouteType.hiking)
            )
          ),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        ),
        impactedNodeIds = Seq.empty,
        impactedNetworkIds = Seq.empty,

      )
    )
  }
}
