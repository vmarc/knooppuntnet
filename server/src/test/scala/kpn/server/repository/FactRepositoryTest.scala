package kpn.server.repository

import kpn.api.common.NetworkFacts
import kpn.api.common.NetworkIntegrityCheckFailed
import kpn.api.common.NodeIntegrityCheck
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteRedundantNodes
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class FactRepositoryTest extends UnitTest with SharedTestObjects {

  test("routeFacts") {

    withCouchDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database, false, null)
      networkRepository.save(
        newNetworkInfo(
          newNetworkAttributes(
            1,
            Some(Country.be),
            NetworkType.hiking,
            name = "network-1"
          ),
          detail = Some(
            newNetworkInfoDetail(
              routes = Seq(
                newNetworkInfoRoute(
                  11,
                  "01-02",
                  facts = Seq(
                    RouteRedundantNodes,
                    RouteUnusedSegments
                  )
                ),
                newNetworkInfoRoute(
                  12,
                  "02-03",
                  facts = Seq(
                    RouteNodeMissingInWays,
                    RouteUnusedSegments
                  )
                )
              )
            )
          )
        )
      )

      networkRepository.save(
        newNetworkInfo(
          newNetworkAttributes(
            2,
            Some(Country.be),
            NetworkType.hiking,
            name = "network-2"
          ),
          detail = Some(
            newNetworkInfoDetail(
              routes = Seq(
                newNetworkInfoRoute(
                  13,
                  "03-04",
                  facts = Seq(
                    RouteUnusedSegments
                  )
                )
              )
            )
          )
        )
      )

      val repository: FactRepository = new FactRepositoryImpl(database)
      repository.factsPerNetwork(Subset.beHiking, RouteUnusedSegments, stale = false) should matchTo(
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
  }

  test("nodeFacts") {

    withCouchDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database, false, null)
      networkRepository.save(
        newNetworkInfo(
          newNetworkAttributes(
            1,
            Some(Country.be),
            NetworkType.hiking,
            name = "network-1"
          ),
          detail = Some(
            newNetworkInfoDetail(
              networkFacts = NetworkFacts(
                integrityCheckFailed = Some(
                  NetworkIntegrityCheckFailed(
                    2,
                    checks = Seq(
                      NodeIntegrityCheck(
                        nodeName = "01",
                        nodeId = 1001,
                        actual = 2,
                        expected = 3,
                        failed = true
                      ),
                      NodeIntegrityCheck(
                        nodeName = "02",
                        nodeId = 1002,
                        actual = 2,
                        expected = 3,
                        failed = true
                      )
                    )
                  )
                )
              )
            )
          )
        )
      )

      val repository: FactRepository = new FactRepositoryImpl(database)
      repository.factsPerNetwork(Subset.beHiking, Fact.IntegrityCheckFailed, stale = false) should matchTo(
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
}
