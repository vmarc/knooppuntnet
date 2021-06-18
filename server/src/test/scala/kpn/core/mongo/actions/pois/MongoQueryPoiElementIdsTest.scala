package kpn.core.mongo.actions.pois

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryPoiElementIdsTest extends UnitTest with SharedTestObjects {

  test("poi element ids") {

    withDatabase { database =>

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
  }

  test("poi element ids - no pois") {
    withDatabase { database =>
      new MongoQueryPoiElementIds(database).execute("node") should equal(Seq.empty)
      new MongoQueryPoiElementIds(database).execute("way") should equal(Seq.empty)
      new MongoQueryPoiElementIds(database).execute("relation") should equal(Seq.empty)
    }
  }
}
