package kpn.core.tools.investigation

import kpn.core.db.couch.Couch
import kpn.core.db.views.AnalyzerDesign
import kpn.core.db.views.NodeNetworkReferenceView

object NetworksWithNodesWithRoleConnection extends App {

  Couch.executeIn("localhost", "master") { database =>
    val nodeNetworkReferences = database.old.query(AnalyzerDesign, NodeNetworkReferenceView, Couch.batchTimeout, stale = false)().map(NodeNetworkReferenceView.convert)
    val networks = nodeNetworkReferences.filter(_.nodeRoleConnection).map(n => (n.networkId, n.networkName)).distinct
    networks.foreach { network =>
      println(s"network id=${network._1}, name=${network._2}")
    }
  }
}
