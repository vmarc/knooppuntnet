package kpn.database.actions.routes

import kpn.core.test.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryKnownRouteIdsTest extends UnitTest with SharedTestObjects {

  test("known route ids") {

    withDatabase { database =>

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(11L)
        )
      )

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(12L),
          active = false // non-active routes are not included
        )
      )

      new MongoQueryKnownRouteIds(database).execute(Seq(11L, 12L, 13L)) should equal(Seq(11L))
    }
  }
}
