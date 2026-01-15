package kpn.server.api.analysis.pages.network

import kpn.api.common.Check
import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkFactsPage

object NetworkFactsPageExample {

  val page: NetworkFactsPage = NetworkFactsPage(
    NetworkDetailsPageExample.networkSummary(),
    Seq(
      NetworkFact(
        Fact.NetworkExtraMemberNode,
        elementType = Some("node"),
        elementIds = Some(Seq(111, 222, 333))
      ),
      NetworkFact(
        Fact.NetworkExtraMemberWay,
        elementType = Some("way"),
        elementIds = Some(Seq(444, 555, 666))
      ),
      NetworkFact(
        Fact.NetworkExtraMemberRelation,
        elementType = Some("relation"),
        elementIds = Some(Seq(777, 888, 999))
      ),
      NetworkFact(
        Fact.IntegrityCheckFailed,
        checks = Some(
          Seq(
            Check(nodeId = 1, nodeName = "01", expected = 3, actual = 1),
            Check(nodeId = 2, nodeName = "02", expected = 3, actual = 2),
            Check(nodeId = 3, nodeName = "03", expected = 3, actual = 4),
            Check(nodeId = 4, nodeName = "04", expected = 3, actual = 5)
          )
        )
      ),
      NetworkFact(Fact.NameMissing),
      NetworkFact(
        Fact.NodeMemberMissing,
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
        Fact.OrphanNode,
        elementType = Some("node"),
        elements = Some(
          Seq(
            Ref(1001, "01")
          )
        )
      ),
      NetworkFact(
        Fact.RouteNotBackward,
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
        Fact.RouteUnusedSegments,
        elementType = Some("route"),
        elements = Some(
          Seq(
            Ref(12, "01-02")
          )
        )
      )
    )
  )
}
