package kpn.database.actions.pois

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newPoi

class MongoQueryPoiElementIdsTest extends MongoTest {

  test("poi element ids") {

    database.pois.save(newPoi("node", 1001))
    database.pois.save(newPoi("node", 1002))
    database.pois.save(newPoi("way", 101))
    database.pois.save(newPoi("way", 102))
    database.pois.save(newPoi("relation", 1))
    database.pois.save(newPoi("relation", 2))

    new MongoQueryPoiElementIds(database).execute("node") should equal(Seq(1001, 1002))
    new MongoQueryPoiElementIds(database).execute("way") should equal(Seq(101, 102))
    new MongoQueryPoiElementIds(database).execute("relation") should equal(Seq(1, 2))
  }

  test("poi element ids - no pois") {
    new MongoQueryPoiElementIds(database).execute("node") should equal(Seq.empty)
    new MongoQueryPoiElementIds(database).execute("way") should equal(Seq.empty)
    new MongoQueryPoiElementIds(database).execute("relation") should equal(Seq.empty)
  }
}
