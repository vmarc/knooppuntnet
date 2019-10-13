package kpn.server.analyzer.engine.changes.integration

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.changes.ChangeAction

class NetworkDelete_Test02 extends AbstractTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    val data = TestData2().data
    val tc = new TestConfig()
    tc.relationBefore(data, 1)

    tc.analysisContext.data.networks.watched.add(1, ElementIds())

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisContext.data.networks.watched.contains(1) should equal(false)

    (tc.networkRepository.save _).verify(*).never()
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveNetworkChange _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }

}
