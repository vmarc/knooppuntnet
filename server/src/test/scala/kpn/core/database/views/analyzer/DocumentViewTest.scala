package kpn.core.database.views.analyzer

import kpn.core.TestObjects
import kpn.core.database.views.analyzer.DocumentView.DocumentCount
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl

class DocumentViewTest extends UnitTest with TestObjects {

  test("allNodeIds") {

    withDatabase { database =>
      val repo = new NodeRepositoryImpl(database)
      repo.save(newNodeInfo(1001))
      repo.save(newNodeInfo(1002))
      repo.save(newNodeInfo(1003))

      DocumentView.allNodeIds(database) should equal(Seq(1001, 1002, 1003))
    }
  }

  test("allRouteIds") {

    withDatabase { database =>
      val repo = newRouteRepository(database)
      repo.save(newRouteInfo(newRouteSummary(10)))
      repo.save(newRouteInfo(newRouteSummary(20)))
      repo.save(newRouteInfo(newRouteSummary(30)))

      DocumentView.allRouteIds(database) should equal(Seq(10, 20, 30))
    }
  }

  test("allNetworkIds") {

    withDatabase { database =>
      val repo = new NetworkRepositoryImpl(database)
      repo.save(newNetworkInfo(newNetworkAttributes(1)))
      repo.save(newNetworkInfo(newNetworkAttributes(2)))
      repo.save(newNetworkInfo(newNetworkAttributes(3)))

      DocumentView.allNetworkIds(database) should equal(Seq(1, 2, 3))
    }
  }

  test("counts") {

    withDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.save(newNodeInfo(1001))
      nodeRepository.save(newNodeInfo(1002))

      val repo = newRouteRepository(database)
      repo.save(newRouteInfo(newRouteSummary(10)))
      repo.save(newRouteInfo(newRouteSummary(20)))
      repo.save(newRouteInfo(newRouteSummary(30)))

      DocumentView.counts(database, AnalyzerDesign) should equal(Seq(DocumentCount("node", 2), DocumentCount("route", 3)))
    }
  }

}
