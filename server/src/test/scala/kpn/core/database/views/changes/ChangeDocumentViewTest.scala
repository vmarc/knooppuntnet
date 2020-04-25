package kpn.core.database.views.changes

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.ChangeSetRepositoryImpl

class ChangeDocumentViewTest extends UnitTest with SharedTestObjects {

  test("view") {

    withDatabase { database =>
      val repo = new ChangeSetRepositoryImpl(database)

      repo.saveChangeSetSummary(newChangeSetSummary(newChangeKey()))

      repo.saveNetworkChange(newNetworkChange(newChangeKey(elementId = 1)))
      repo.saveNetworkChange(newNetworkChange(newChangeKey(elementId = 2)))

      repo.saveRouteChange(newRouteChange(newChangeKey(elementId = 11)))
      repo.saveRouteChange(newRouteChange(newChangeKey(elementId = 12)))

      repo.saveNodeChange(newNodeChange(newChangeKey(elementId = 1001)))
      repo.saveNodeChange(newNodeChange(newChangeKey(elementId = 1002)))

      ChangeDocumentView.allNetworkIds(database) should equal(Seq(1L, 2L))
      ChangeDocumentView.allRouteIds(database) should equal(Seq(11L, 12L))
      ChangeDocumentView.allNodeIds(database) should equal(Seq(1001L, 1002L))
    }
  }
}
