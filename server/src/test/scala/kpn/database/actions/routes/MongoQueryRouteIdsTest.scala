package kpn.database.actions.routes

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newRouteDoc

class MongoQueryRouteIdsTest extends MongoTest {

  test("active route ids") {

    database.routes.save(newRouteDoc(11L))
    database.routes.save(newRouteDoc(12L))
    database.routes.save(newRouteDoc(13L, active = false))

    new MongoQueryRouteIds(database).execute() should equal(Seq(11L, 12L))
  }
}
