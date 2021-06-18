package kpn.core.mongo.actions.pois

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryPoiAllTilesTest extends UnitTest with SharedTestObjects {

  test("poi element ids") {
    withDatabase { database =>

      database.pois.save(newPoi("way", 1, tiles = Seq("tile-2")))
      database.pois.save(newPoi("way", 2, tiles = Seq("tile-1")))
      database.pois.save(newPoi("way", 3, tiles = Seq("tile-2", "tile-3")))

      new MongoQueryPoiAllTiles(database).execute() should equal(Seq("tile-1", "tile-2", "tile-3"))
    }
  }

  test("poi element ids - no pois") {
    withDatabase { database =>
      new MongoQueryPoiAllTiles(database).execute() should equal(Seq.empty)
    }
  }
}