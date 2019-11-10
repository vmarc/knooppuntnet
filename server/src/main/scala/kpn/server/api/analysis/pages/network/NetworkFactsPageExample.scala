package kpn.server.api.analysis.pages.network

import kpn.api.common.Check
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkFactsPage
import kpn.api.custom.Fact

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

}
