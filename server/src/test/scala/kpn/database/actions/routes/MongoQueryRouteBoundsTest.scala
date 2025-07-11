package kpn.database.actions.routes

import kpn.api.common.Bounds
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteSummary

class MongoQueryRouteBoundsTest extends MongoTest {

  test("bounds") {

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
        active = false, // route does not have 'active' label
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
