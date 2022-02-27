package kpn.database.actions.routes

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryKnownRouteIdsTest extends UnitTest with SharedTestObjects {

  test("known route ids") {

    withDatabase { database =>

      database.routes.save(
        newRouteDoc(
          newRouteSummary(11L)
        )
      )

      database.routes.save(
        newRouteDoc(
          newRouteSummary(12L),
          labels = Seq.empty // non-active routes are not included
        )
      )

      new MongoQueryKnownRouteIds(database).execute(Seq(11L, 12L, 13L)) should equal(Seq(11L))
    }
  }
}
