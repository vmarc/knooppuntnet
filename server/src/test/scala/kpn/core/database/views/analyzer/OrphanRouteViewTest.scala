package kpn.core.database.views.analyzer

import kpn.api.common.OrphanRouteInfo
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Country
import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.database.Database
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.RouteRepositoryImpl

class OrphanRouteViewTest extends UnitTest with SharedTestObjects {

  test("orphan routes are included in the view") {

    pending // no 'orphan' in RouteInfo anymore

    withCouchDatabase { database =>
      val orphanRouteInfos = route(database, orphan = true)
      orphanRouteInfos should matchTo(
        Seq(
          OrphanRouteInfo(
            11,
            "01-02",
            123,
            isBroken = true,
            accessible = true,
            lastSurvey = "2020-08-11",
            lastUpdated = defaultTimestamp
          )
        )
      )
    }
  }

  test("regular routes are not included in the view") {
    withCouchDatabase { database =>
      val orphanRouteInfos = route(database, orphan = false)
      orphanRouteInfos shouldBe empty
    }
  }

  test("inactive orphan routes are not included in the view") {
    withCouchDatabase { database =>
      val orphanRouteInfos = route(database, orphan = true, active = false)
      orphanRouteInfos shouldBe empty
    }
  }

  private def route(database: Database, orphan: Boolean, active: Boolean = true): Seq[OrphanRouteInfo] = {

    val routeInfo = newRoute(
      11,
      active = active,
      orphan = orphan,
      country = Some(Country.nl),
      networkType = NetworkType.hiking,
      name = "01-02",
      meters = 123,
      facts = Seq(Fact.RouteBroken),
      lastSurvey = Some(Day(2020, 8, Some(11)))
    )

    val routeRepository = new RouteRepositoryImpl(null)
    routeRepository.save(routeInfo)

    OrphanRouteView.query(database, Subset.nlHiking, stale = false)
  }
}
