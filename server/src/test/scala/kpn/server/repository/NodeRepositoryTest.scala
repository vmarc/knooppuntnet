package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class NodeRepositoryTest extends UnitTest with SharedTestObjects {

  test("nodeWithId") {

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeDoc(101))
      nodeRepository.save(newNodeDoc(102))
      nodeRepository.save(newNodeDoc(103))

      assertEqual(nodeRepository.nodeWithId(101), Some(newNodeDoc(101)))
      assertEqual(nodeRepository.nodeWithId(102), Some(newNodeDoc(102)))
      assertEqual(nodeRepository.nodeWithId(103), Some(newNodeDoc(103)))
      nodeRepository.nodeWithId(104) should equal(None)
    }
  }

  test("nodesWithIds") {

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeDoc(101))
      nodeRepository.save(newNodeDoc(102))

      assertEqual(nodeRepository.nodesWithIds(Seq(101, 102, 103)), Seq(newNodeDoc(101), newNodeDoc(102)))
    }
  }

  test("save") {

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.save(newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.save(newNodeDoc(103, tags = Tags.from("rwn_ref" -> "03")))

      assertEqual(nodeRepository.nodeWithId(101), Some(newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01"))))
      assertEqual(nodeRepository.nodeWithId(102), Some(newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02"))))
      assertEqual(nodeRepository.nodeWithId(103), Some(newNodeDoc(103, tags = Tags.from("rwn_ref" -> "03"))))
      nodeRepository.nodeWithId(104) should equal(None)

      nodeRepository.bulkSave(
        newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01")),
        newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02")),
        newNodeDoc(103, tags = Tags.from("rwn_ref" -> "03"))
      )

      assertEqual(nodeRepository.nodeWithId(101), Some(newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01"))))
      assertEqual(nodeRepository.nodeWithId(102), Some(newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02"))))
      assertEqual(nodeRepository.nodeWithId(103), Some(newNodeDoc(103, tags = Tags.from("rwn_ref" -> "03"))))
      nodeRepository.nodeWithId(104) should equal(None)

      nodeRepository.bulkSave(
        newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01")),
        newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02")),
        newNodeDoc(103, tags = Tags.from("rwn_ref" -> "33"))
      )

      assertEqual(nodeRepository.nodeWithId(101), Some(newNodeDoc(101, tags = Tags.from("rwn_ref" -> "01"))))
      assertEqual(nodeRepository.nodeWithId(102), Some(newNodeDoc(102, tags = Tags.from("rwn_ref" -> "02"))))
      assertEqual(nodeRepository.nodeWithId(103), Some(newNodeDoc(103, tags = Tags.from("rwn_ref" -> "33")))) // updated
      nodeRepository.nodeWithId(104) should equal(None)
    }
  }

  test("save same network multiple times (delete before save)") {

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeDoc(101))
      nodeRepository.save(newNodeDoc(101))
      nodeRepository.save(newNodeDoc(101))

      assertEqual(nodeRepository.nodeWithId(101), Some(newNodeDoc(101)))
    }
  }

  test("filterKnown") {

    withDatabase { database =>
      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeDoc(101))
      nodeRepository.save(newNodeDoc(102))

      nodeRepository.filterKnown(Set(101, 102, 103)) should equal(Set(101, 102))
    }
  }

  test("nodeNetworkReferences") {

    withDatabase { database =>

      pending

      //  new NetworkRepositoryImpl(database).oldSaveNetworkInfo(
      //    newNetworkInfo(
      //      newNetworkAttributes(
      //        2,
      //        name = "network-2"
      //      ),
      //      nodeRefs = Seq(1001)
      //    )
      //  )
      //
      //  val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
      //  nodeRepository.nodeNetworkReferences(1001)should equal(
      //    Seq(
      //      Reference(
      //        routeType.hiking,
      //        NetworkScope.regional,
      //        2,
      //        "network-2"
      //      )
      //    )
      //  )
    }
  }
}
