package kpn.core.database.views.analyzer

import kpn.core.database.Database
import kpn.core.database.views.analyzer.OrphanRouteView.OrphanRouteKey
import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.RouteRepositoryImpl
import kpn.shared.{Country, NetworkType, RouteSummary, SharedTestObjects}
import org.scalatest.{FunSuite, Matchers}

class OrphanRouteViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("orphan routes are included in the view") {

    withDatabase { database =>

      val rows = route(database, orphan = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanRouteKey(orphan = true, "nl", "rwn", 11)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          11
        )
      )
    }
  }

  test("regular routes are not included in the view") {
    withDatabase { database =>
      val rows = route(database, orphan = false)
      rows should equal(Seq())
    }
  }

  test("inactive orphan routes are not included in the view") {
    withDatabase { database =>
      val rows = route(database, orphan = true, active = false)
      rows should equal(Seq())
    }
  }

  private def route(database: Database, orphan: Boolean, active: Boolean = true): Seq[(OrphanRouteKey, RouteSummary)] = {
    val routeInfo = newRoute(
      11,
      active = active,
      orphan = orphan,
      country = Some(Country.nl),
      networkType = NetworkType.hiking
    )

    val routeRepository = new RouteRepositoryImpl(database)
    routeRepository.save(routeInfo)

    database.old.query(AnalyzerDesign, OrphanRouteView, Couch.uiTimeout, stale = false)().map(OrphanRouteView.toKeyAndValue)
  }
}
