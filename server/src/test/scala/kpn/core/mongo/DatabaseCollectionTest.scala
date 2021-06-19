package kpn.core.mongo

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class DatabaseCollectionTest extends UnitTest with SharedTestObjects {

  test("ids") {
    withDatabase { database =>
      database.networks.save(newNetworkInfo(newNetworkAttributes(1L)))
      database.networks.save(newNetworkInfo(newNetworkAttributes(2L)))
      database.networks.ids() should equal(Seq(1L, 2L))
    }
  }

  test("ids from empty collection") {
    withDatabase { database =>
      database.networks.ids() should equal(Seq.empty)
    }
  }

  test("findById") {
    withDatabase { database =>
      val networkInfo = newNetworkInfo(newNetworkAttributes(1L))
      database.networks.save(networkInfo)
      database.networks.findById(1L) should equal(Some(networkInfo))
    }
  }

  test("findById - not found") {
    withDatabase { database =>
      database.networks.findById(1L) should equal(None)
    }
  }

  test("findByIds") {
    withDatabase { database =>
      val networkInfo1 = newNetworkInfo(newNetworkAttributes(1L))
      val networkInfo2 = newNetworkInfo(newNetworkAttributes(2L))
      val networkInfo3 = newNetworkInfo(newNetworkAttributes(3L))
      database.networks.insertMany(Seq(networkInfo1, networkInfo2, networkInfo3))

      database.networks.findByIds(Seq(1L, 2L)) should equal(
        Seq(
          networkInfo1,
          networkInfo2
        )
      )
      database.networks.findByIds(Seq(2L, 3L)) should equal(
        Seq(
          networkInfo2,
          networkInfo3
        )
      )
    }
  }

  test("findByIds - not found") {
    withDatabase { database =>
      database.networks.findByIds(Seq(1L, 2L)) should equal(Seq.empty)
    }
  }
}
