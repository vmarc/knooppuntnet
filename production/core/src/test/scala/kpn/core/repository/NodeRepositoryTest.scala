package kpn.core.repository

import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.common.Reference
import kpn.shared.data.Tags
import kpn.shared.node.NodeReferences
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeRepositoryTest extends FunSuite with Matchers with SharedTestObjects {

  test("nodeWithId") {

    withDatabase { database =>

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

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.nodesWithIds(Seq(101, 102, 103), Couch.uiTimeout, stale = false) should equal(Seq(newNodeInfo(101), newNodeInfo(102)))
    }
  }

  test("nodeReferences") {

    withDatabase { database =>

      new NetworkRepositoryImpl(database).save(
        newNetwork(
          1,
          Some(Country.be),
          NetworkType.hiking,
          "network-name",
          nodes = Seq(
            newNetworkNodeInfo2(101, "01"),
            newNetworkNodeInfo2(102, "02")
          )
        )
      )

      new RouteRepositoryImpl(database).save(
        newRoute(
          id = 10,
          name = "route-name",
          analysis = newRouteInfoAnalysis(
            startNodes = Seq(
              newRouteNetworkNodeInfo(101, "01")
            )
          )
        )
      )

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.nodeReferences(101, Couch.uiTimeout, stale = false) should equal(
        NodeReferences(
          Seq(Reference(1, "network-name", NetworkType.hiking)),
          Seq(Reference(10, "route-name", NetworkType.hiking))
        )
      )
    }
  }

  test("save") {

    withDatabase { database =>

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

    withDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))

      nodeRepository.nodeWithId(101, Couch.uiTimeout) should equal(Some(newNodeInfo(101)))
    }
  }

  test("filterKnown") {
    withDatabase { database =>
      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.filterKnown(Set(101, 102, 103)) should equal(Set(101, 102))
    }
  }

}
