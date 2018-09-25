package kpn.core.db.views

import kpn.core.db.couch.Couch
import kpn.core.db.couch.Database
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck
import kpn.shared.SharedTestObjects
import kpn.shared.common.Ref
import kpn.shared.node.NodeNetworkIntegrityCheck
import kpn.shared.node.NodeNetworkReference
import kpn.shared.node.NodeNetworkRouteReference
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeNetworkReferenceViewTest extends FunSuite with Matchers with SharedTestObjects {

  private val timeout = Couch.uiTimeout

  test("node network reference") {

    withDatabase { database =>
      val networkRepository = new NetworkRepositoryImpl(database)
      networkRepository.save(
        newNetwork(
          1,
          name = "network-1",
          nodes = Seq(
            newNetworkNodeInfo2(
              1001,
              "01",
              definedInRelation = true,
              roleConnection = true,
              routeReferences = Seq(Ref(10, "01-02"))
            ),
            newNetworkNodeInfo2(
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
            newNetworkRouteInfo(
              10,
              "01-02",
              role = Some("connection")
            )
          )
        )
      )

      networkRepository.save(
        newNetwork(
          2,
          name = "network-2",
          nodes = Seq(
            newNetworkNodeInfo2(
              1001,
              "01",
              definedInRelation = true
            )
          )
        )
      )

      queryNode(database, 1001) should equal(
        Seq(
          NodeNetworkReference(
            networkId = 1,
            networkType = NetworkType.hiking,
            networkName = "network-1",
            nodeDefinedInRelation = true,
            nodeRoleConnection = true,
            nodeIntegrityCheck = None,
            routes = Seq(
              NodeNetworkRouteReference(
                routeId = 10,
                routeName = "01-02",
                routeRole = Some("connection")
              )
            )
          ),
          NodeNetworkReference(
            networkId = 2,
            networkType = NetworkType.hiking,
            networkName = "network-2",
            nodeDefinedInRelation = true,
            nodeRoleConnection = false,
            nodeIntegrityCheck = None,
            routes = Seq()
          )
        )
      )

      queryNode(database, 1002) should equal(
        Seq(
          NodeNetworkReference(
            networkId = 1,
            networkType = NetworkType.hiking,
            networkName = "network-1",
            nodeDefinedInRelation = true,
            nodeRoleConnection = false,
            nodeIntegrityCheck = Some(
              NodeNetworkIntegrityCheck(
                failed = true,
                expected = 3,
                actual = 1
              )
            ),
            routes = Seq(
              NodeNetworkRouteReference(
                routeId = 10,
                routeName = "01-02",
                routeRole = Some("connection")
              )
            )
          )
        )
      )
    }
  }

  def queryNode(database: Database, nodeId: Long): Seq[NodeNetworkReference] = {
    database.query(AnalyzerDesign, NodeNetworkReferenceView, timeout, stale = false)(nodeId).map(NodeNetworkReferenceView.convert)
  }

}
