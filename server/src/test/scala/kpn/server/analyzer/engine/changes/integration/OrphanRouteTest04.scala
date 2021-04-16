package kpn.server.analyzer.engine.changes.integration

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.test.TestData2
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction

class OrphanRouteTest04 extends AbstractTest with SharedTestObjects {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    val dataEmpty = TestData2().data
    val tc = new TestConfig()
    tc.relationBefore(dataEmpty, 11)
    tc.analysisContext.data.orphanRoutes.watched.add(11, ElementIds())
    tc.relationAfter(dataEmpty, 11)

    tc.process(ChangeAction.Delete, newRawRelation(11))

    assert(!tc.analysisContext.data.orphanRoutes.watched.contains(11))

    (tc.analysisRepository.saveRoute _).verify(*).never() // too difficult to save meaningful data
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }
}
