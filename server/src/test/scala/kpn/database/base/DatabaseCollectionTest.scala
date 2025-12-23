package kpn.database.base

import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNodeChange

class DatabaseCollectionTest extends MongoTest {

  test("ids") {
    database.networks.save(newNetworkDoc(1L))
    database.networks.save(newNetworkDoc(2L))
    database.networks.ids() should equal(Seq(1L, 2L))
  }

  test("ids from empty collection") {
    database.networks.ids() should equal(Seq.empty)
  }

  test("stringIds") {
    database.nodeChanges.save(newNodeChange(newChangeKey(elementId = 1001L)))
    database.nodeChanges.save(newNodeChange(newChangeKey(elementId = 1002L)))
    database.nodeChanges.stringIds() should equal(Seq("1:1:1001", "1:1:1002"))
  }

  test("stringIds from empty collection") {
    database.nodeChanges.stringIds() should equal(Seq.empty)
  }

  test("findById") {
    val network = newNetworkDoc(1L)
    database.networks.save(network)
    database.networks.findById(1L) should equal(Some(network))
  }

  test("findById - not found") {
    database.networks.findById(1L) should equal(None)
  }

  test("findByIds") {
    val network1 = newNetworkDoc(1L)
    val network2 = newNetworkDoc(2L)
    val network3 = newNetworkDoc(3L)
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

  test("findByIds - not found") {
    database.networks.findByIds(Seq(1L, 2L)) should equal(Seq.empty)
  }
}
