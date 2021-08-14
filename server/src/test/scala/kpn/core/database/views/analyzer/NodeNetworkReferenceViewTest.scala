package kpn.core.database.views.analyzer

import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.database.Database
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest
import kpn.server.repository.NetworkRepositoryImpl

class NodeNetworkReferenceViewTest extends UnitTest with SharedTestObjects {

  test("node network reference") {

    withCouchDatabase { database =>
      val networkRepository = new NetworkRepositoryImpl(null)
      networkRepository.oldSaveNetworkInfo(buildNetworkWithNode1001and1002())
      networkRepository.oldSaveNetworkInfo(buildNetworkWithNode1001())

      queryNode(database, 1001) should matchTo(
        Seq(
          Reference(
            NetworkType.hiking,
            NetworkScope.regional,
            1,
            "network-1",
          ),
          Reference(
            NetworkType.hiking,
            NetworkScope.regional,
            2,
            "network-2"
          )
        )
      )

      queryNode(database, 1002) should matchTo(
        Seq(
          Reference(
            NetworkType.hiking,
            NetworkScope.regional,
            1,
            "network-1"
          )
        )
      )
    }
  }

  test("no node network references when network not active") {

    withCouchDatabase { database =>
      val networkRepository = new NetworkRepositoryImpl(null)
      networkRepository.oldSaveNetworkInfo(buildInactiveNetwork())
      queryNode(database, 1001) shouldBe empty
    }
  }

  def queryNode(database: Database, nodeId: Long): Seq[Reference] = {
    pending
    //NodeNetworkReferenceView.query(database, nodeId, stale = false)
    Seq.empty
  }

  private def buildNetworkWithNode1001and1002(): NetworkInfo = {
    newNetworkInfo(
      newNetworkAttributes(
        1,
        name = "network-1"
      ),
      detail = Some(
        newNetworkInfoDetail(
          nodes = Seq(
            newNetworkInfoNode(
              1001,
              "01",
              definedInRelation = true,
              connection = true,
              roleConnection = true,
              routeReferences = Seq(Ref(10, "01-02"))
            ),
            newNetworkInfoNode(
              1002,
              "02",
              definedInRelation = true,
              routeReferences = Seq(Ref(10, "01-02")),
              integrityCheck = Some(
                NodeIntegrityCheck(
                  nodeName = "02",
                  nodeId = 1002,
                  actual = 1,
                  expected = 3,
                  failed = true
                )
              )
            )
          ),
          routes = Seq(
            newNetworkInfoRoute(
              10,
              "01-02",
              role = Some("connection")
            )
          )
        )
      )
    )
  }

  private def buildNetworkWithNode1001(): NetworkInfo = {
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
  }

  private def buildInactiveNetwork(): NetworkInfo = {
    newNetworkInfo(
      newNetworkAttributes(
        3,
        name = "network-3"
      ),
      active = false,
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
  }
}
