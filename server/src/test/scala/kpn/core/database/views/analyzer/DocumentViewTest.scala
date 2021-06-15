package kpn.core.database.views.analyzer

import kpn.core.TestObjects
import kpn.core.database.views.analyzer.DocumentView.DocumentCount
import kpn.core.gpx.GpxFile
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.NodeRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class DocumentViewTest extends UnitTest with TestObjects {

  test("allNodeIds") {

    withCouchDatabase { database =>
      val repo = new NodeRepositoryImpl(database, false, null)
      repo.save(newNodeInfo(1001))
      repo.save(newNodeInfo(1002))
      repo.save(newNodeInfo(1003))

      DocumentView.allNodeIds(database) should equal(Seq(1001, 1002, 1003))
    }
  }

  test("allRouteIds") {

    withCouchDatabase { database =>
      val repo = new RouteRepositoryImpl(database, false, null)
      repo.save(newRouteInfo(newRouteSummary(10)))
      repo.save(newRouteInfo(newRouteSummary(20)))
      repo.save(newRouteInfo(newRouteSummary(30)))

      DocumentView.allRouteIds(database) should equal(Seq(10, 20, 30))
    }
  }

  test("allNetworkIds") {

    withCouchDatabase { database =>
      val repo = new NetworkRepositoryImpl(database, false, null)
      repo.save(newNetworkInfo(newNetworkAttributes(1)))
      repo.save(newNetworkInfo(newNetworkAttributes(2)))
      repo.save(newNetworkInfo(newNetworkAttributes(3)))
      repo.saveGpxFile(GpxFile(4, 4, "4", Seq.empty, Seq.empty))
      repo.saveGpxFile(GpxFile(5, 5, "4", Seq.empty, Seq.empty))

      DocumentView.allNetworkIds(database) should equal(Seq(1, 2, 3))
    }
  }

  test("counts") {

    withCouchDatabase { database =>

      val nodeRepository = new NodeRepositoryImpl(database, false, null)
      nodeRepository.save(newNodeInfo(1001))
      nodeRepository.save(newNodeInfo(1002))

      val repo = new RouteRepositoryImpl(database, false, null)
      repo.save(newRouteInfo(newRouteSummary(10)))
      repo.save(newRouteInfo(newRouteSummary(20)))
      repo.save(newRouteInfo(newRouteSummary(30)))

      DocumentView.counts(database, AnalyzerDesign) should matchTo(Seq(DocumentCount("node", 2), DocumentCount("route", 3)))
    }
  }
}
