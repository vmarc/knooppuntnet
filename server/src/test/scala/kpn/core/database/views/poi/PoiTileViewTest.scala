package kpn.core.database.views.poi

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.poi.PoiRef
import kpn.server.repository.PoiRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiTileViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("allTiles") {

    withDatabase { database =>

      val repo = new PoiRepositoryImpl(database)

      repo.save(newPoi("node", 1001, tiles = Seq("10-1-1", "10-1-2")))
      repo.save(newPoi("node", 1002, tiles = Seq("10-2-1")))
      repo.save(newPoi("way", 101, tiles = Seq("10-2-1"))) // same tile as 2nd node

      PoiTileView.allTiles(database, stale = false) should equal(Seq("10-1-1", "10-1-2", "10-2-1"))
    }
  }

  test("tilePoiRefs") {

    withDatabase { database =>

      val repo = new PoiRepositoryImpl(database)

      repo.save(newPoi("node", 1001, tiles = Seq("10-1-1", "10-1-2")))
      repo.save(newPoi("node", 1002, tiles = Seq("10-2-1")))
      repo.save(newPoi("way", 101, tiles = Seq("10-2-1"))) // same tile as 2nd node

      PoiTileView.tilePoiRefs("10-1-1", database, stale = false) should equal(
        Seq(
          PoiRef("node", 1001)
        )
      )

      PoiTileView.tilePoiRefs("10-2-1", database, stale = false) should equal(
        Seq(
          PoiRef("node", 1002),
          PoiRef("way", 101),
        )
      )
    }
  }
}
