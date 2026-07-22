package kpn.server.analyzer.engine.changes.route.main

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.RouteType
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetContext
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.util.UnitTest
import org.scalamock.stubs.Stubs

class RouteChangeDeleteProcessorTest extends UnitTest with Stubs {
  test("delete") {
    // setup
    val processor = new RouteChangeDeleteProcessor()
    val context = newChangeSetContext()
    val routeDoc = newRouteDoc()

    // execute
    val routeChangeContext = processor.process(context, routeDoc)

    // verify
    assertEqual(
      routeChangeContext.get,
      RouteChangeContext(
        routeChange = newRouteChange(
          key = newChangeKey(elementId = 1),
          changeType = ChangeType.Delete,
          before = Some(
            newRouteData(
              relationId = 1,
              routeTypes = Seq(RouteType.hiking)
            )
          ),
          facts = Seq(Fact.Deleted),
          investigate = true,
          impact = true,
          locationInvestigate = true,
          locationImpact = true
        ),
        impactedNodeIds = Seq.empty,
        impactedNetworkIds = Seq.empty
      )
    )
  }
}
