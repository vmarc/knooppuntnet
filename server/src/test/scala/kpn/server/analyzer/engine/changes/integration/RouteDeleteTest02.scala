package kpn.server.analyzer.engine.changes.integration

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.test.OverpassData
import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction
import kpn.core.data.Data

class RouteDeleteTest02 extends AbstractTest with SharedTestObjects {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    val dataEmpty = OverpassData().data
    val tc = new OldTestConfig(Data.empty, Data.empty)
    tc.relationBefore(dataEmpty, 11)
    tc.analysisContext.data.routes.watched.add(11, ElementIds())
    tc.relationAfter(dataEmpty, 11)

    tc.process(ChangeAction.Delete, newRawRelation(11))

    assert(!tc.analysisContext.data.routes.watched.contains(11))

    (tc.routeRepository.save _).verify(*).never() // too difficult to save meaningful data
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }
}
