package kpn.database.actions.pois

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newPoi

class MongoQueryPoiAllTilesTest extends MongoTest {

  test("poi element ids") {

    database.pois.save(newPoi("way", 1, tiles = Seq("tile-2")))
    database.pois.save(newPoi("way", 2, tiles = Seq("tile-1")))
    database.pois.save(newPoi("way", 3, tiles = Seq("tile-2", "tile-3")))

    new MongoQueryPoiAllTiles(database).execute() should equal(Seq("tile-1", "tile-2", "tile-3"))
  }

  test("poi element ids - no pois") {
    new MongoQueryPoiAllTiles(database).execute() should equal(Seq.empty)
  }
}
