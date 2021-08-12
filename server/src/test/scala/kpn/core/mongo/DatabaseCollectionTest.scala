package kpn.core.mongo

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class DatabaseCollectionTest extends UnitTest with SharedTestObjects {

  test("ids") {
    withDatabase { database =>
      database.networks.save(newNetwork(1L))
      database.networks.save(newNetwork(2L))
      database.networks.ids() should equal(Seq(1L, 2L))
    }
  }

  test("ids from empty collection") {
    withDatabase { database =>
      database.networks.ids() should equal(Seq.empty)
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
      val network = newNetwork(1L)
      database.networks.save(network)
      database.networks.findById(1L) should equal(Some(network))
    }
  }

  test("findById - not found") {
    withDatabase { database =>
      database.networks.findById(1L) should equal(None)
    }
  }

  test("findByIds") {
    withDatabase { database =>
      val network1 = newNetwork(1L)
      val network2 = newNetwork(2L)
      val network3 = newNetwork(3L)
      database.networks.insertMany(Seq(network1, network2, network3))

      database.networks.findByIds(Seq(1L, 2L)) should equal(
        Seq(
          network1,
          network2
        )
      )
      database.networks.findByIds(Seq(2L, 3L)) should equal(
        Seq(
          network2,
          network3
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
