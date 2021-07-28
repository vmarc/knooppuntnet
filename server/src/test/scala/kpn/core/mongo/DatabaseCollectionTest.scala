package kpn.core.mongo

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class DatabaseCollectionTest extends UnitTest with SharedTestObjects {

  test("ids") {
    withDatabase { database =>
      database.oldNetworks.save(newNetworkInfo(newNetworkAttributes(1L)))
      database.oldNetworks.save(newNetworkInfo(newNetworkAttributes(2L)))
      database.oldNetworks.ids() should equal(Seq(1L, 2L))
    }
  }

  test("ids from empty collection") {
    withDatabase { database =>
      database.oldNetworks.ids() should equal(Seq.empty)
    }
  }

  test("stringIds") {
    withDatabase { database =>
      database.nodeChanges.save(newNodeChange(newChangeKey(elementId = 1001L)))
      database.nodeChanges.save(newNodeChange(newChangeKey(elementId = 1002L)))
      database.nodeChanges.stringIds() should equal(Seq("123:1:1001", "123:1:1002"))
    }
  }

  test("stringIds from empty collection") {
    withDatabase { database =>
      database.nodeChanges.stringIds() should equal(Seq.empty)
    }
  }

  test("findById") {
    withDatabase { database =>
      val networkInfo = newNetworkInfo(newNetworkAttributes(1L))
      database.oldNetworks.save(networkInfo)
      database.oldNetworks.findById(1L) should equal(Some(networkInfo))
    }
  }

  test("findById - not found") {
    withDatabase { database =>
      database.oldNetworks.findById(1L) should equal(None)
    }
  }

  test("findByIds") {
    withDatabase { database =>
      val networkInfo1 = newNetworkInfo(newNetworkAttributes(1L))
      val networkInfo2 = newNetworkInfo(newNetworkAttributes(2L))
      val networkInfo3 = newNetworkInfo(newNetworkAttributes(3L))
      database.oldNetworks.insertMany(Seq(networkInfo1, networkInfo2, networkInfo3))

      database.oldNetworks.findByIds(Seq(1L, 2L)) should equal(
        Seq(
          networkInfo1,
          networkInfo2
        )
      )
      database.oldNetworks.findByIds(Seq(2L, 3L)) should equal(
        Seq(
          networkInfo2,
          networkInfo3
        )
      )
    }
  }

  test("findByIds - not found") {
    withDatabase { database =>
      database.oldNetworks.findByIds(Seq(1L, 2L)) should equal(Seq.empty)
    }
  }
}
