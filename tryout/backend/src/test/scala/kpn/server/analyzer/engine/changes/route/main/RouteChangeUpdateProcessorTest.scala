package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.RouteType
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeUpdateProcessorTest extends UnitTest with Stubs {
  test("update") {
    // setup
    val processor = new RouteChangeUpdateProcessor()
    val context = newChangeSetContext()
    val routeDocBefore = newRouteDoc()
    val routeDocAfter = newRouteDoc()

    // execute
    val routeChangeContext = processor.process(context, routeDocBefore, routeDocAfter, 1)

    // verify
    assertEqual(
      routeChangeContext.get,
      RouteChangeContext(
        routeChange = newRouteChange(
          key = newChangeKey(elementId = 1),
          changeType = ChangeType.Update,
          before = Some(
            newRouteData(
              relationId = 1,
              routeTypes = Seq(RouteType.hiking)
            )
          ),
          after = Some(
            newRouteData(
              relationId = 1,
              routeTypes = Seq(RouteType.hiking)
            )
          )
        ),
        impactedNodeIds = Seq.empty,
        impactedNetworkIds = Seq.empty
      )
    )
  }
}
