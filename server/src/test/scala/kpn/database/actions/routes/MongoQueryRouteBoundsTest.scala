package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryRouteBoundsTest extends UnitTest with SharedTestObjects {

  test("bounds") {

    withDatabase { database =>

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(11L),
          bounds = Some(Bounds(1, 2, 3, 4))
        )
      )

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(12L),
          bounds = Some(Bounds(5, 6, 7, 8))
        )
      )

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(13L),
          labels = Seq.empty, // route does not have 'active' label
          bounds = Some(Bounds(9, 10, 11, 12))
        )
      )

      database.baseRoutes.save(
        newBaseRouteDoc(
          newRouteSummary(14L),
          bounds = None // superroutes with no ways or nodes do not contain bounds
        )
      )

      new MongoQueryRouteBounds(database).execute(Seq(11L, 12L, 13L, 14L)) should equal(Some(Bounds(1, 2, 7, 8)))
    }
  }
}
