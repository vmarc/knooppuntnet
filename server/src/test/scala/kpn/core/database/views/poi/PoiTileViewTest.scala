package kpn.core.database.views.poi

import kpn.api.common.SharedTestObjects
import kpn.core.database.Database
import kpn.core.poi.PoiInfo
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.PoiRepositoryImpl

class PoiTileViewTest extends UnitTest with SharedTestObjects {

  test("allTiles") {
    withCouchDatabase { database =>
      setupData(database)
      PoiTileView.allTiles(database, stale = false) should equal(Seq("10-1-1", "10-1-2", "10-2-1"))
    }
  }

  test("tilePoiRefs") {

    withCouchDatabase { database =>

      setupData(database)

      PoiTileView.tilePoiInfos("10-1-1", database, stale = false) should matchTo(
        Seq(
          PoiInfo("node", 1001, "1", "1", "bench")
        )
      )

      PoiTileView.tilePoiInfos("10-2-1", database, stale = false) should matchTo(
        Seq(
          PoiInfo("node", 1002, "2", "2", "bench"),
          PoiInfo("way", 101, "3", "3", "bench"),
        )
      )
    }
  }

  private def setupData(database: Database): Unit = {

    val repo = new PoiRepositoryImpl(null, database, false)

    repo.save(
      newPoi(
        "node",
        1001,
        latitude = "1",
        longitude = "1",
        layers = Seq("bench"),
        tiles = Seq(
          "10-1-1",
          "10-1-2"
        )
      )
    )
    repo.save(
      newPoi(
        "node",
        1002,
        latitude = "2",
        longitude = "2",
        layers = Seq("bench"),
        tiles = Seq(
          "10-2-1"
        )
      )
    )
    repo.save(
      newPoi(
        "way",
        101,
        latitude = "3",
        longitude = "3",
        layers = Seq("bench"),
        tiles = Seq(
          "10-2-1" // same tile as 2nd node
        )
      )
    )
  }
}
