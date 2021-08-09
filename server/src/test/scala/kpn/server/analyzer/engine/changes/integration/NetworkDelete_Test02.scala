package kpn.server.analyzer.engine.changes.integration

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.test.TestData2
import kpn.api.common.changes.ChangeAction
import kpn.core.data.Data

class NetworkDelete_Test02 extends AbstractTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    pending

    val dataBefore = TestData2().data
    val tc = new OldTestConfig(dataBefore, Data.empty)

    tc.analysisContext.data.networks.watched.add(1, ElementIds())

    tc.process(ChangeAction.Delete, newRawRelation(1))

    assert(!tc.analysisContext.data.networks.watched.contains(1))

    (tc.networkRepository.oldSaveNetworkInfo _).verify(*).never()
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveNetworkChange _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }

}
