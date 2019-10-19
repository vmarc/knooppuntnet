package kpn.server.repository

import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withOldDatabase
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeRepositoryTest extends FunSuite with Matchers with SharedTestObjects {

  test("nodeWithId") {

    withOldDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))
      nodeRepository.save(newNodeInfo(103))

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101)))
      nodeRepository.nodeWithId(102, Couch.uiTimeout) should equal(Some(newNodeInfo(102)))
      nodeRepository.nodeWithId(103, Couch.uiTimeout) should equal(Some(newNodeInfo(103)))
      nodeRepository.nodeWithId(104, Couch.uiTimeout) should equal(None)
    }
  }

  test("nodesWithIds") {

    withOldDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.nodesWithIds(Seq(101, 102, 103), Couch.uiTimeout, stale = false) should equal(Seq(newNodeInfo(101), newNodeInfo(102)))
    }
  }

  test("save") {

    withOldDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(
        newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
        newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
        newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))
      ) should equal(true)

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01"))))
      nodeRepository.nodeWithId(102, Couch.uiTimeout) should equal(Some(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02"))))
      nodeRepository.nodeWithId(103, Couch.uiTimeout) should equal(Some(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))))
      nodeRepository.nodeWithId(104, Couch.uiTimeout) should equal(None)

      nodeRepository.save(
        newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
        newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
        newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))
      ) should equal(false)

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01"))))
      nodeRepository.nodeWithId(102, Couch.uiTimeout) should equal(Some(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02"))))
      nodeRepository.nodeWithId(103, Couch.uiTimeout) should equal(Some(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))))
      nodeRepository.nodeWithId(104, Couch.uiTimeout) should equal(None)

      nodeRepository.save(
        newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
        newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
        newNodeInfo(103, tags = Tags.from("rwn_ref" -> "33"))
      ) should equal(true)

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01"))))
      nodeRepository.nodeWithId(102, Couch.uiTimeout) should equal(Some(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02"))))
      nodeRepository.nodeWithId(103, Couch.uiTimeout) should equal(Some(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "33")))) // updated
      nodeRepository.nodeWithId(104, Couch.uiTimeout) should equal(None)
    }
  }

  test("save same network multiple times (delete before save)") {

    withOldDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101)))
    }
  }

  test("filterKnown") {
    withOldDatabase { database =>
      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.filterKnown(Set(101, 102, 103)) should equal(Set(101, 102))
    }
  }

}
