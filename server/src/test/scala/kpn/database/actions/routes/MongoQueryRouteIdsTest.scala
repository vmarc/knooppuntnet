package kpn.database.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteIdsTest extends UnitTest with SharedTestObjects {

  test("active route ids") {

    withDatabase { database =>

      database.routes.save(newRouteDoc(newRouteSummary(11L)))
      database.routes.save(newRouteDoc(newRouteSummary(12L)))
      database.routes.save(newRouteDoc(newRouteSummary(13L), active = false))

      new MongoQueryRouteIds(database).execute() should equal(Seq(11L, 12L))
    }
  }
}
