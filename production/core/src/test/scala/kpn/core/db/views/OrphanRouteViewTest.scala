package kpn.core.db.views

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.db.views.OrphanRouteView.OrphanRouteKey
import kpn.core.repository.RouteRepositoryImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.RouteSummary
import kpn.shared.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class OrphanRouteViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("orphan routes are included in the view") {

    withDatabase { database =>

      val rows = route(database, orphan = true, ignored = false, display = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanRouteKey(ignored = false, orphan = true, display = true, "nl", "rwn", 11)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          11
        )
      )
    }
  }

  test("ignored orphan routes are included in the view") {

    withDatabase { database =>

      val rows = route(database, orphan = true, ignored = true, display = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanRouteKey(ignored = true, orphan = true, display = true, "nl", "rwn", 11)
        )
      )

      rows.map(_._2.id) should equal(
        Seq(
          11
        )
      )
    }
  }

  test("ignored routes are included in the view") {

    withDatabase { database =>

      val rows = route(database, orphan = false, ignored = true, display = true)

      rows.map(_._1) should equal(
        Seq(
          OrphanRouteKey(ignored = true, orphan = false, display = true, "nl", "rwn", 11)
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
      val rows = route(database, orphan = false, ignored = false, display = true)
      rows should equal(Seq())
    }
  }

  private def route(database: Database, orphan: Boolean, ignored: Boolean, display: Boolean): Seq[(OrphanRouteKey, RouteSummary)] = {
    val routeInfo = newRoute(
      11,
      display = display,
      ignored = ignored,
      orphan = orphan,
      country = Some(Country.nl),
      networkType = NetworkType.hiking
    )

    val routeRepository = new RouteRepositoryImpl(database)
    routeRepository.save(routeInfo)

    database.query(AnalyzerDesign, OrphanRouteView, Couch.uiTimeout, stale = false)().map(OrphanRouteView.toKeyAndValue)
  }
}
