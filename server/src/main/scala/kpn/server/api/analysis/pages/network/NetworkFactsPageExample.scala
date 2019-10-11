package kpn.server.api.analysis.pages.network

import kpn.shared.Check
import kpn.shared.Fact
import kpn.shared.NetworkExtraMemberNode
import kpn.shared.NetworkExtraMemberRelation
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFact
import kpn.shared.NetworkFacts
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NetworkNameMissing
import kpn.shared.NodeIntegrityCheck
import kpn.shared.common.Ref
import kpn.shared.network.NetworkFactsPage
import kpn.shared.network.NetworkNodeFact
import kpn.shared.network.NetworkRouteFact
import kpn.shared.network.OldNetworkFactsPage

object NetworkFactsPageExample {

  val page = NetworkFactsPage(
    NetworkDetailsPageExample.networkSummary(),
    Seq(
      NetworkFact(
        Fact.NetworkExtraMemberNode.name,
        elementType = Some("node"),
        elementIds = Some(Seq(111, 222, 333))
      ),
      NetworkFact(
        Fact.NetworkExtraMemberWay.name,
        elementType = Some("way"),
        elementIds = Some(Seq(444, 555, 666))
      ),
      NetworkFact(
        Fact.NetworkExtraMemberRelation.name,
        elementType = Some("relation"),
        elementIds = Some(Seq(777, 888, 999))
      ),
      NetworkFact(
        Fact.IntegrityCheckFailed.name,
        checks = Some(
          Seq(
            Check(nodeId = 1, nodeName = "01", actual = 1, expected = 3),
            Check(nodeId = 2, nodeName = "02", actual = 2, expected = 3),
            Check(nodeId = 3, nodeName = "03", actual = 4, expected = 3),
            Check(nodeId = 4, nodeName = "04", actual = 5, expected = 3)
          )
        )
      ),
      NetworkFact(Fact.NameMissing.name),
      NetworkFact(
        Fact.NodeMemberMissing.name,
        elementType = Some("node"),
        elements = Some(
          Seq(
            Ref(1001, "01"),
            Ref(1002, "02"),
            Ref(1003, "03")
          )
        )
      ),
      NetworkFact(
        Fact.OrphanNode.name,
        elementType = Some("node"),
        elements = Some(
          Seq(
            Ref(1001, "01")
          )
        )
      ),
      NetworkFact(
        Fact.RouteNotBackward.name,
        elementType = Some("route"),
        elements = Some(
          Seq(
            Ref(12, "01-02"),
            Ref(13, "01-03"),
            Ref(14, "01-04"),
            Ref(15, "01-05")
          )
        )
      ),
      NetworkFact(
        Fact.RouteUnusedSegments.name,
        elementType = Some("route"),
        elements = Some(
          Seq(
            Ref(12, "01-02")
          )
        )
      )
    )
  )

  val oldPage = OldNetworkFactsPage(
    NetworkDetailsPageExample.networkSummary(),
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
    nodeFacts = Seq(
      NetworkNodeFact(
        fact = Fact.NodeMemberMissing,
        nodes = Seq(
          Ref(1001, "01"),
          Ref(1002, "02"),
          Ref(1003, "03")
        )
      ),
      NetworkNodeFact(
        fact = Fact.OrphanNode,
        nodes = Seq(
          Ref(1001, "01")
        )
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
      ),
      NetworkRouteFact(
        fact = Fact.RouteUnusedSegments,
        routes = Seq(
          Ref(12, "01-02")
        )
      )
    ),
    facts = Seq(
    )
  )

}
