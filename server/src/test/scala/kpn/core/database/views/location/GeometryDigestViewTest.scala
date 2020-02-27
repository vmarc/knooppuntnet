package kpn.core.database.views.location

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.RouteRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class GeometryDigestViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("route geometry digest") {
    withDatabase { database =>
      val routeRepository = newRouteRepository(database)
      routeRepository.save(
        newRoute(
          id = 11,
          analysis = newRouteInfoAnalysis(
            geometryDigest = "abc"
          )
        )
      )

      GeometryDigestView.query(database, 11, stale = false) should equal(Some("abc"))
    }
  }

  test("route not found") {
    withDatabase { database =>
      GeometryDigestView.query(database, 11, stale = false) should equal(None)
    }
  }

}
