package kpn.server.repository

import kpn.api.custom.Tags
import kpn.core.test.MongoTest

class NodeRepositoryTest extends MongoTest {

  test("nodeWithId") {

    val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

    nodeRepository.save(newNodeDoc(1001))
    nodeRepository.save(newNodeDoc(1002))
    nodeRepository.save(newNodeDoc(1003))

    assertEqual(nodeRepository.nodeWithId(1001), Some(newNodeDoc(1001)))
    assertEqual(nodeRepository.nodeWithId(1002), Some(newNodeDoc(1002)))
    assertEqual(nodeRepository.nodeWithId(1003), Some(newNodeDoc(1003)))
    nodeRepository.nodeWithId(104) should equal(None)
  }

  test("nodesWithIds") {

    val nodeRepository = new NodeRepositoryImpl(database)

    val node1001 = newNodeDoc(1001)
    val node1002 = newNodeDoc(1002, active = false)

    nodeRepository.save(node1001)
    nodeRepository.save(node1002)

    assertEqual(
      nodeRepository.nodesWithIds(Seq(1001, 1002, 1003)),
      Seq(
        node1001,
        node1002
      )
    )
  }

  test("activeNodesWithIds") {

    val nodeRepository = new NodeRepositoryImpl(database)

    val node1001 = newNodeDoc(1001)
    val node1002 = newNodeDoc(1002, active = false)

    nodeRepository.save(node1001)
    nodeRepository.save(node1002)

    assertEqual(
      nodeRepository.activeNodesWithIds(Seq(1001, 1002, 1003)),
      Seq(
        node1001
      )
    )
  }

  test("save") {

    val nodeRepository = new NodeRepositoryImpl(database)

    nodeRepository.save(newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01")))
    nodeRepository.save(newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02")))
    nodeRepository.save(newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "03")))

    assertEqual(nodeRepository.nodeWithId(1001), Some(newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01"))))
    assertEqual(nodeRepository.nodeWithId(1002), Some(newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02"))))
    assertEqual(nodeRepository.nodeWithId(1003), Some(newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "03"))))
    nodeRepository.nodeWithId(104) should equal(None)

    nodeRepository.bulkSave(
      newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01")),
      newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02")),
      newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "03"))
    )

    assertEqual(nodeRepository.nodeWithId(1001), Some(newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01"))))
    assertEqual(nodeRepository.nodeWithId(1002), Some(newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02"))))
    assertEqual(nodeRepository.nodeWithId(1003), Some(newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "03"))))
    nodeRepository.nodeWithId(104) should equal(None)

    nodeRepository.bulkSave(
      newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01")),
      newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02")),
      newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "33"))
    )

    assertEqual(nodeRepository.nodeWithId(1001), Some(newNodeDoc(1001, tags = Tags.from("rwn_ref" -> "01"))))
    assertEqual(nodeRepository.nodeWithId(1002), Some(newNodeDoc(1002, tags = Tags.from("rwn_ref" -> "02"))))
    assertEqual(nodeRepository.nodeWithId(1003), Some(newNodeDoc(1003, tags = Tags.from("rwn_ref" -> "33")))) // updated
    nodeRepository.nodeWithId(104) should equal(None)
  }

  test("save same network multiple times (delete before save)") {

    val nodeRepository = new NodeRepositoryImpl(database)

    nodeRepository.save(newNodeDoc(1001))
    nodeRepository.save(newNodeDoc(1001))
    nodeRepository.save(newNodeDoc(1001))

    assertEqual(
      nodeRepository.nodeWithId(1001),
      Some(newNodeDoc(1001))
    )
  }

  test("filterKnown") {

    val nodeRepository = new NodeRepositoryImpl(database)

    nodeRepository.save(newNodeDoc(1001))
    nodeRepository.save(newNodeDoc(1002))

    nodeRepository.filterKnown(Set(1001, 1002, 1003)) should equal(Set(1001, 1002))
  }
}
