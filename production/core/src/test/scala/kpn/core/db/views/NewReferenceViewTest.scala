package kpn.core.db.views

import kpn.core.db.couch.{Couch, Database}
import kpn.core.repository.NetworkRepositoryImpl
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.common.Ref
import kpn.shared.node.{NodeNetworkIntegrityCheck, NodeNetworkReference, NodeNetworkRouteReference}
import kpn.shared.{NetworkType, NodeIntegrityCheck, SharedTestObjects}
import org.scalatest.{FunSuite, Matchers}

class NewReferenceViewTest extends FunSuite with Matchers with SharedTestObjects {

  private val timeout = Couch.uiTimeout

  test("view n") {

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
              connection = true,
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
            nodeConnection = true,
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
            nodeConnection = false,
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
            nodeConnection = false,
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
    database.query(AnalyzerDesign, NewReferenceView, timeout, stale = false)(nodeId).map(NewReferenceView.convert)
  }

}
