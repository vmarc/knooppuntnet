package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.api.common.node.NodeNetworkReference
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class NodeRepositoryTest extends UnitTest with SharedTestObjects {

  test("nodeWithId") {

    withCouchDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))
      nodeRepository.save(newNodeInfo(103))

      nodeRepository.nodeWithId(101).value should matchTo(newNodeInfo(101))
      nodeRepository.nodeWithId(102).value should matchTo(newNodeInfo(102))
      nodeRepository.nodeWithId(103).value should matchTo(newNodeInfo(103))
      nodeRepository.nodeWithId(104) should equal(None)
    }
  }

  test("nodesWithIds") {

    withCouchDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.nodesWithIds(Seq(101, 102, 103), stale = false) should matchTo(Seq(newNodeInfo(101), newNodeInfo(102)))
    }
  }

  test("save") {

    withCouchDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      assert(
        nodeRepository.save(
          newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
          newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
          newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))
        )
      )

      nodeRepository.nodeWithId(101).value should equal(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.nodeWithId(102).value should equal(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.nodeWithId(103).value should equal(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03")))
      nodeRepository.nodeWithId(104) should equal(None)

      assert(
        !nodeRepository.save(
          newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
          newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
          newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03"))
        )
      )

      nodeRepository.nodeWithId(101).value should matchTo(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.nodeWithId(102).value should matchTo(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.nodeWithId(103).value should matchTo(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "03")))
      nodeRepository.nodeWithId(104) should equal(None)

      assert(
        nodeRepository.save(
          newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")),
          newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")),
          newNodeInfo(103, tags = Tags.from("rwn_ref" -> "33"))
        )
      )

      nodeRepository.nodeWithId(101).value should matchTo(newNodeInfo(101, tags = Tags.from("rwn_ref" -> "01")))
      nodeRepository.nodeWithId(102).value should matchTo(newNodeInfo(102, tags = Tags.from("rwn_ref" -> "02")))
      nodeRepository.nodeWithId(103).value should matchTo(newNodeInfo(103, tags = Tags.from("rwn_ref" -> "33"))) // updated
      nodeRepository.nodeWithId(104) should equal(None)
    }
  }

  test("save same network multiple times (delete before save)") {

    withCouchDatabase { database =>

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(101))

      nodeRepository.nodeWithId(101).value should matchTo(newNodeInfo(101))
    }
  }

  test("filterKnown") {
    withCouchDatabase { database =>
      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)

      nodeRepository.save(newNodeInfo(101))
      nodeRepository.save(newNodeInfo(102))

      nodeRepository.filterKnown(Set(101, 102, 103)) should equal(Set(101, 102))
    }
  }

  test("nodeNetworkReferences") {

    withCouchDatabase { database =>

      new NetworkRepositoryImpl(database, false, null).save(
        newNetworkInfo(
          newNetworkAttributes(
            2,
            name = "network-2"
          ),
          detail = Some(
            newNetworkInfoDetail(
              nodes = Seq(
                newNetworkInfoNode(
                  1001,
                  "01",
                  definedInRelation = true
                )
              )
            )
          )
        )
      )

      val nodeRepository: NodeRepository = new NodeRepositoryImpl(database)
      nodeRepository.nodeNetworkReferences(1001, stale = false) should matchTo(
        Seq(
          NodeNetworkReference(
            NetworkType.hiking,
            2,
            "network-2",
            nodeDefinedInRelation = true,
            nodeConnection = false,
            nodeRoleConnection = false,
            None,
            Seq(),
            Seq()
          )
        )
      )
    }
  }
}
