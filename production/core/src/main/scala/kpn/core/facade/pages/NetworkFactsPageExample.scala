package kpn.core.facade.pages

import kpn.shared.Fact
import kpn.shared.Fact.f
import kpn.shared.FactLevel
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFacts
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NetworkNameMissing
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck
import kpn.shared.common.Ref
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkRouteFact
import kpn.shared.network.NetworkSummary

object NetworkFactsPageExample {

  val page = NetworkFactsPage(
    NetworkSummary(
      NetworkType.hiking,
      "network name",
      3,
      4,
      5
    ),
    NetworkFacts(
      networkExtraMemberNode = Some(
        Seq(
          NetworkExtraMemberNode(111),
          NetworkExtraMemberNode(222),
          NetworkExtraMemberNode(333)
        )
      ),
      networkExtraMemberWay = Some(
        Seq(
          NetworkExtraMemberWay(444),
          NetworkExtraMemberWay(555),
          NetworkExtraMemberWay(666)
        )
      ),
      networkExtraMemberRelation = Some(
        Seq(
          NetworkExtraMemberRelation(777),
          NetworkExtraMemberRelation(888),
          NetworkExtraMemberRelation(999)
        )
      ),
      integrityCheck = None,
      integrityCheckFailed = Some(
        NetworkIntegrityCheckFailed(
          count = 3,
          checks = Seq(
            NodeIntegrityCheck(nodeName = "01", nodeId = 1, actual = 1, expected = 3, failed = true),
            NodeIntegrityCheck(nodeName = "02", nodeId = 2, actual = 2, expected = 3, failed = true),
            NodeIntegrityCheck(nodeName = "03", nodeId = 3, actual = 4, expected = 3, failed = true),
            NodeIntegrityCheck(nodeName = "04", nodeId = 4, actual = 5, expected = 3, failed = true)
          )
        )
      ),
      nameMissing = Some(
        NetworkNameMissing()
      )
    ),
    routeFacts = Seq(
      NetworkRouteFact(
        fact = Fact.RouteNotBackward,
        routes = Seq(
          Ref(12, "01-02"),
          Ref(13, "01-03"),
          Ref(14, "01-04"),
          Ref(15, "01-05")
        )
      )
    ),
    facts = Seq(
      Fact.IgnoreForeignCountry,
      Fact.IgnoreNotNodeNetwork,
      Fact.IgnoreNetworkTaggedAsRoute,
      Fact.IgnoreNoNetworkNodes,
      Fact.IgnoreNetworkCollection,
      Fact.IgnoreUnsupportedSubset
    )
  )

}
