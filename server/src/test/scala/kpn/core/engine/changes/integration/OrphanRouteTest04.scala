package kpn.core.engine.changes.integration

import kpn.core.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.SharedTestObjects
import kpn.shared.changes.ChangeAction

class OrphanRouteTest04 extends AbstractTest with SharedTestObjects {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    val dataEmpty = TestData2().data
    val tc = new TestConfig()
    tc.relationBefore(dataEmpty, 11)
    tc.analysisContext.data.orphanRoutes.watched.add(11, ElementIds())
    tc.relationAfter(dataEmpty, 11)

    tc.process(ChangeAction.Delete, newRawRelation(11))

    tc.analysisContext.data.orphanRoutes.watched.contains(11) should equal(false)

    (tc.analysisRepository.saveRoute _).verify(*).never() // too difficult to save meaningful data
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }
}
