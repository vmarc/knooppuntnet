package kpn.database.actions.routes

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newRouteSummary

class MongoQueryKnownRouteIdsTest extends MongoTest {

  test("known route ids") {

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
