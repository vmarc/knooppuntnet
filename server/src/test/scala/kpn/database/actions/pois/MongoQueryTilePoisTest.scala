package kpn.database.actions.pois

import kpn.api.common.SharedTestObjects
import kpn.core.poi.PoiInfo
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryTilePoisTest extends UnitTest with SharedTestObjects {

  test("tile pois") {
    withDatabase { database =>

      database.pois.save(
        newPoi(
          "node",
          1,
          latitude = "11",
          longitude = "12",
          tiles = Seq("tile-1"),
          layers = Seq("bar")
        )
      )
      database.pois.save(
        newPoi(
          "node",
          2,
          latitude = "21",
          longitude = "22",
          tiles = Seq("tile-1", "tile-2"),
          layers = Seq("bench", "windmill")
        )
      )
      database.pois.save(
        newPoi(
          "node",
          3,
          latitude = "31",
          longitude = "32",
          tiles = Seq("tile-2"),
          layers = Seq("bench")
        )
      )
      database.pois.save(
        newPoi(
          "node",
          4,
          latitude = "41",
          longitude = "42",
          tiles = Seq("tile-2", "tile-3"),
          layers = Seq("bench")
        )
      )

      new MongoQueryTilePois(database).execute("tile-1").shouldMatchTo(
        Seq(
          PoiInfo("node", 1, "11", "12", "bar"),
          PoiInfo("node", 2, "21", "22", "bench"),
          PoiInfo("node", 2, "21", "22", "windmill"),
        )
      )

      new MongoQueryTilePois(database).execute("tile-2").shouldMatchTo(
        Seq(
          PoiInfo("node", 2, "21", "22", "bench"),
          PoiInfo("node", 2, "21", "22", "windmill"),
          PoiInfo("node", 3, "31", "32", "bench"),
          PoiInfo("node", 4, "41", "42", "bench"),
        )
      )

      new MongoQueryTilePois(database).execute("tile-3").shouldMatchTo(
        Seq(
          PoiInfo("node", 4, "41", "42", "bench"),
        )
      )

      new MongoQueryTilePois(database).execute("tile-4") should equal(Seq.empty)
    }
  }
}
