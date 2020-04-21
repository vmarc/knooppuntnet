package kpn.core.database.views.poi

import kpn.api.common.SharedTestObjects
import kpn.core.database.Database
import kpn.core.poi.PoiInfo
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.PoiRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class PoiTileViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("allTiles") {
    withDatabase { database =>
      setupData(database)
      PoiTileView.allTiles(database, stale = false) should equal(Seq("10-1-1", "10-1-2", "10-2-1"))
    }
  }

  test("tilePoiRefs") {

    withDatabase { database =>

      setupData(database)

      PoiTileView.tilePoiInfos("10-1-1", database, stale = false) should equal(
        Seq(
          PoiInfo("node", 1001, "1", "1", "bench")
        )
      )

      PoiTileView.tilePoiInfos("10-2-1", database, stale = false) should equal(
        Seq(
          PoiInfo("node", 1002, "2", "2", "bench"),
          PoiInfo("way", 101, "3", "3", "bench"),
        )
      )
    }
  }

  private def setupData(database: Database): Unit = {

    val repo = new PoiRepositoryImpl(database)

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
