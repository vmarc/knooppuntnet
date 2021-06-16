package kpn.core.mongo.actions.base

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class MongoQueryIdsTest extends UnitTest with SharedTestObjects {

  test("ids") {
    withDatabase { database =>
      database.networks.save(newNetworkInfo(newNetworkAttributes(1L)))
      database.networks.save(newNetworkInfo(newNetworkAttributes(2L)))
      val result = new MongoQueryIds(database).execute("networks")
      result should equal(Seq(1L, 2L))
    }
  }

  test("ids from empty collection") {
    withDatabase { database =>
      val result = new MongoQueryIds(database).execute("networks")
      result should equal(Seq.empty)
    }
  }
}
