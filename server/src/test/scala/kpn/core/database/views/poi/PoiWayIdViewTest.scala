package kpn.core.database.views.poi

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.server.repository.PoiRepositoryImpl
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiWayIdViewTest extends FunSuite with Matchers with SharedTestObjects {

  test("all id's of pois of type 'way'") {

    withDatabase { database =>

      val repo = new PoiRepositoryImpl(database)

      repo.save(newPoi("way", 101))
      repo.save(newPoi("way", 102))
      repo.save(newPoi("node", 1001))
      repo.save(newPoi("relation", 1))

      PoiWayIdView.query(database, stale = false) should equal(Seq(101, 102))
    }
  }
}
