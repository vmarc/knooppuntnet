package kpn.core.mongo.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteNameInfo
import kpn.api.custom.NetworkType.hiking
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteNameInfoTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteNameInfo(database)

      database.routes.save(buildRoute(11L, "01-02"))
      database.routes.save(buildRoute(12L, "02-03"))
      database.routes.save(buildRoute(13L, "03-04", active = false))

      query.execute(11L) should equal(Some(RouteNameInfo(11L, "01-02", hiking)))
      query.execute(12L) should equal(Some(RouteNameInfo(12L, "02-03", hiking)))
      query.execute(13L) should equal(None)
    }
  }

  private def buildRoute(id: Long, name: String, active: Boolean = true): RouteInfo = {
    newRouteInfo(
      newRouteSummary(
        id,
        networkType = hiking,
        name = name
      ),
      labels = if (active) Seq("active") else Seq.empty,
      active = active
    )
  }
}
