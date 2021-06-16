package kpn.core.mongo

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class DatabaseCollectionTest extends UnitTest with SharedTestObjects {

  test("ids") {
    withDatabase { database =>
      database.networks.save(newNetworkInfo(newNetworkAttributes(1L)))
      database.networks.save(newNetworkInfo(newNetworkAttributes(2L)))
      database.networks.ids should equal(Seq(1L, 2L))
    }
  }

  test("ids from empty collection") {
    withDatabase { database =>
      database.networks.ids should equal(Seq.empty)
    }
  }
}
