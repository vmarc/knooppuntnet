package kpn.database.actions.routes

import kpn.core.test.MongoTest

class MongoQueryRouteIdsTest extends MongoTest {

  test("active route ids") {

    database.routes.save(newRouteDoc(newRouteSummary(11L)))
    database.routes.save(newRouteDoc(newRouteSummary(12L)))
    database.routes.save(newRouteDoc(newRouteSummary(13L), active = false))

    new MongoQueryRouteIds(database).execute() should equal(Seq(11L, 12L))
  }
}
