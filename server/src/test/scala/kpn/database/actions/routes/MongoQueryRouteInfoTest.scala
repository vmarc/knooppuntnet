package kpn.database.actions.routes

import kpn.api.common.RouteType.hiking
import kpn.api.common.route.RouteInfo
import kpn.core.doc.RouteDoc
import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteInfoTest extends UnitTest with SharedTestObjects {

  test("execute") {
    withDatabase { database =>
      val query = new MongoQueryRouteInfo(database)

      database.routes.save(buildRoute(11L, "01-02"))
      database.routes.save(buildRoute(12L, "02-03"))
      database.routes.save(buildRoute(13L, "03-04", active = false))

      query.execute(11L) should equal(Some(RouteInfo(11L, "01-02", Seq(hiking), 0, 0)))
      query.execute(12L) should equal(Some(RouteInfo(12L, "02-03", Seq(hiking), 0, 0)))
      query.execute(13L) should equal(Some(RouteInfo(13L, "03-04", Seq(hiking), 0, 0)))
    }
  }

  private def buildRoute(id: Long, name: String, active: Boolean = true): RouteDoc = {
    newRouteDoc(
      newRouteSummary(
        id,
        routeTypes = Seq(hiking),
        name = name
      ),
      active = active,
      segments = Seq.empty
    )
  }
}
