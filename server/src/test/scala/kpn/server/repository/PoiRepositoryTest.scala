package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.core.poi.PoiInfo
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class PoiRepositoryTest extends UnitTest with SharedTestObjects {

  test("allPois") {

    withRepository { repository =>

      repository.save(newPoi("node", 1, layers = Seq("bar")))
      repository.save(newPoi("node", 2, layers = Seq("bench")))
      repository.save(newPoi("node", 3, layers = Seq("restaurant")))
      repository.save(newPoi("node", 4, layers = Seq("windmill")))
      repository.save(newPoi("node", 5, layers = Seq("windmill")))

      repository.allPois(stale = false) should equal(
        Seq(
          PoiInfo("node", 1, "", "", "bar"),
          PoiInfo("node", 2, "", "", "bench"),
          PoiInfo("node", 3, "", "", "restaurant"),
          PoiInfo("node", 4, "", "", "windmill"),
          PoiInfo("node", 5, "", "", "windmill")
        )
      )
    }
  }

  private def withRepository(f: PoiRepository => Unit): Unit = {
    withDatabase { database =>
      f(new PoiRepositoryImpl(database))
    }
  }

}
