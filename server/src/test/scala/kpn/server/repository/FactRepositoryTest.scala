package kpn.server.repository

import kpn.api.common.SharedTestObjects
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class FactRepositoryTest extends UnitTest with SharedTestObjects {

  test("routeFacts") {

    withDatabase { database =>

      pending

      //      val networkRepository = new NetworkRepositoryImpl(database)
      //      networkRepository.saveNetworkInfo(
      //        newNetworkInfoDoc(
      //          country = Some(Country.be),
      //          summary = newNetworkSummary(
      //            name = "network-1",
      //            networkType = NetworkType.hiking,
      //          ),
      //          detail = Some(
      //            newNetworkInfoDetail(
      //              routes = Seq(
      //                newNetworkInfoRoute(
      //                  11,
      //                  "01-02",
      //                  facts = Seq(
      //                    RouteRedundantNodes,
      //                    RouteUnusedSegments
      //                  )
      //                ),
      //                newNetworkInfoRoute(
      //                  12,
      //                  "02-03",
      //                  facts = Seq(
      //                    RouteNodeMissingInWays,
      //                    RouteUnusedSegments
      //                  )
      //                )
      //              )
      //            )
      //          )
      //        )
      //      )
      //
      //      networkRepository.saveNetworkInfo(
      //        newNetworkInfoDoc(
      //          country = Some(Country.be),
      //          summary = newNetworkSummary(
      //            name = "network-2",
      //            networkType = NetworkType.hiking,
      //          ),
      //          detail = Some(
      //            newNetworkInfoDetail(
      //              routes = Seq(
      //                newNetworkInfoRoute(
      //                  13,
      //                  "03-04",
      //                  facts = Seq(
      //                    RouteUnusedSegments
      //                  )
      //                )
      //              )
      //            )
      //          )
      //        )
      //      )
      //
      //      val repository: FactRepository = new FactRepositoryImpl(null)
      //      repository.factsPerNetwork(Subset.beHiking, RouteUnusedSegments).shouldMatchTo(
      //        Seq(
      //          NetworkFactRefs(
      //            1,
      //            "network-1",
      //            Seq(
      //              Ref(11, "01-02"),
      //              Ref(12, "02-03")
      //            )
      //          ),
      //          NetworkFactRefs(
      //            2,
      //            "network-2",
      //            Seq(
      //              Ref(13, "03-04")
      //            )
      //          )
      //        )
      //      )
    }
  }

  test("nodeFacts") {

    withDatabase { database =>

      pending

      //      val networkRepository = new NetworkRepositoryImpl(database)
      //      networkRepository.saveNetworkInfo(
      //        newNetworkInfoDoc(
      //          newNetworkAttributes(
      //            1,
      //            Some(Country.be),
      //            NetworkType.hiking,
      //            name = "network-1"
      //          ),
      //          detail = Some(
      //            newNetworkInfoDetail(
      //              networkFacts = NetworkFacts(
      //                integrityCheckFailed = Some(
      //                  NetworkIntegrityCheckFailed(
      //                    2,
      //                    checks = Seq(
      //                      NodeIntegrityCheck(
      //                        nodeName = "01",
      //                        nodeId = 1001,
      //                        actual = 2,
      //                        expected = 3,
      //                        failed = true
      //                      ),
      //                      NodeIntegrityCheck(
      //                        nodeName = "02",
      //                        nodeId = 1002,
      //                        actual = 2,
      //                        expected = 3,
      //                        failed = true
      //                      )
      //                    )
      //                  )
      //                )
      //              )
      //            )
      //          )
      //        )
      //      )
      //
      //      val repository: FactRepository = new FactRepositoryImpl(null)
      //      repository.factsPerNetwork(Subset.beHiking, Fact.IntegrityCheckFailed).shouldMatchTo(
      //        Seq(
      //          NetworkFactRefs(
      //            1,
      //            "network-1",
      //            Seq(
      //              Ref(1001, "01"),
      //              Ref(1002, "02")
      //            )
      //          )
      //        )
      //      )
    }
  }
}
