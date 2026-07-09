package kpn.server.repository

import kpn.api.common.Check
import kpn.api.common.Country
import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.RouteType
import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Subset
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkBaseData
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkRouteDetail
import kpn.core.test.TestObjects.newRaw

class FactRepositoryTest extends MongoTest {

  test("routeFacts") {

    val networkRepository = new NetworkRepository(database)
    networkRepository.save(
      newNetworkDoc(
        1,
        base = newNetworkBaseData(
          name = Some("network-1"),
          routeType = RouteType.hiking,
        ),
        country = Some(Country.be),
        routes = Seq(
          newNetworkRouteDetail(
            11,
            "01-02",
            facts = Seq(
              Fact.RouteRedundantNodes,
              Fact.RouteUnusedSegments
            )
          ),
          newNetworkRouteDetail(
            12,
            "02-03",
            facts = Seq(
              Fact.RouteNodeMissingInWays,
              Fact.RouteUnusedSegments
            )
          )
        )
      )
    )

    networkRepository.save(
      newNetworkDoc(
        2,
        base = newNetworkBaseData(
          name = Some("network-2"),
          routeType = RouteType.hiking,
        ),
        country = Some(Country.be),
        routes = Seq(
          newNetworkRouteDetail(
            13,
            "03-04",
            facts = Seq(
              Fact.RouteUnusedSegments
            )
          )
        )
      )
    )

    pendingRedesignNonAnalysis() // used in SubsetFactDetailsPageBuilder and FactCheckTool

    val repository = new FactRepository(database)
    assertEqual(
      repository.factsPerNetwork(Subset.beHiking, Fact.RouteUnusedSegments),
      Seq(
        NetworkFactRefs(
          1,
          "network-1",
          Seq(
            Ref(11, "01-02"),
            Ref(12, "02-03")
          )
        ),
        NetworkFactRefs(
          2,
          "network-2",
          Seq(
            Ref(13, "03-04")
          )
        )
      )
    )
  }

  test("nodeFacts") {

    val networkRepository = new NetworkRepository(database)
    networkRepository.save(
      newNetworkDoc(
        1,
        base = newNetworkBaseData(
          raw = newRaw(
          ),
          name = Some("network-1"),
          routeType = RouteType.hiking,
        ),
        country = Some(Country.be),
        facts = Seq(
          NetworkFact(
            Fact.IntegrityCheckFailed,
            checks = Some(
              Seq(
                Check(
                  nodeId = 1001,
                  nodeName = "01",
                  actual = 2,
                  expected = 3
                ),
                Check(
                  nodeId = 1002,
                  nodeName = "02",
                  actual = 2,
                  expected = 3
                )
              )
            )
          )
        )
      )
    )

    pendingRedesignNonAnalysis() // used in SubsetFactDetailsPageBuilder and FactCheckTool

    val repository = new FactRepository(database)
    assertEqual(
      repository.factsPerNetwork(Subset.beHiking, Fact.IntegrityCheckFailed),
      Seq(
        NetworkFactRefs(
          1,
          "network-1",
          Seq(
            Ref(1001, "01"),
            Ref(1002, "02")
          )
        )
      )
    )
  }
}
