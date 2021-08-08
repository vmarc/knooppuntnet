package kpn.core.database.views.analyzer

import kpn.core.TestObjects
import kpn.core.gpx.GpxFile
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl
import kpn.server.repository.RouteRepositoryImpl

class DocumentViewTest extends UnitTest with TestObjects {

  test("allRouteIds") {

    withCouchDatabase { database =>
      val repo = new RouteRepositoryImpl(null, database, false)
      repo.save(newRouteInfo(newRouteSummary(10)))
      repo.save(newRouteInfo(newRouteSummary(20)))
      repo.save(newRouteInfo(newRouteSummary(30)))

      DocumentView.allRouteIds(database) should equal(Seq(10, 20, 30))
    }
  }

  test("allNetworkIds") {

    withCouchDatabase { database =>
      val repo = new NetworkRepositoryImpl(null, database, false)
      repo.oldSaveNetworkInfo(newNetworkInfo(newNetworkAttributes(1)))
      repo.oldSaveNetworkInfo(newNetworkInfo(newNetworkAttributes(2)))
      repo.oldSaveNetworkInfo(newNetworkInfo(newNetworkAttributes(3)))
      repo.saveGpxFile(GpxFile(4, 4, "4", Seq.empty, Seq.empty))
      repo.saveGpxFile(GpxFile(5, 5, "4", Seq.empty, Seq.empty))

      DocumentView.allNetworkIds(database) should equal(Seq(1, 2, 3))
    }
  }

}
