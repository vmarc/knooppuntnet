package kpn.core.repository

import kpn.core.app.IntegrityCheckPage
import kpn.core.app.NetworkIntegrityInfo
import kpn.core.db.couch.Couch
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.Fact.RouteNodeMissingInWays
import kpn.shared.Fact.RouteRedundantNodes
import kpn.shared.Fact.RouteUnusedSegments
import kpn.shared.NetworkFacts
import kpn.shared.NetworkIntegrityCheckFailed
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck
import kpn.shared.RoutesFact
import kpn.shared.SharedTestObjects
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.subset.NetworkRoutesFacts
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactRepositoryTest extends FunSuite with Matchers with SharedTestObjects {

  test("routeFacts") {

    withDatabase { database =>

      val networkRepository: NetworkRepository = new NetworkRepositoryImpl(database)
      networkRepository.save(
        newNetwork(
          1,
          Some(Country.be),
          NetworkType.hiking,
          "network-1",
          routes = Seq(
            newNetworkRouteInfo(
              11,
              "01-02",
              facts = Seq(
                RouteRedundantNodes,
                RouteUnusedSegments
              )
            ),
            newNetworkRouteInfo(
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

      networkRepository.save(
        newNetwork(
          2,
          Some(Country.be),
          NetworkType.hiking,
          "network-2",
          routes = Seq(
            newNetworkRouteInfo(
              13,
              "03-04",
              facts = Seq(
                RouteUnusedSegments
              )
            )
          )
        )
      )

      val repository: FactRepository = new FactRepositoryImpl(database)
      repository.routeFacts(Subset.beHiking, RouteUnusedSegments, Couch.uiTimeout, stale = false) should equal(
        Seq(
          NetworkRoutesFacts(
            1,
            "network-1",
            RoutesFact(
              Seq(
                Ref(11, "01-02"),
                Ref(12, "02-03")
              )
            )
          ),
          NetworkRoutesFacts(
            2,
            "network-2",
            RoutesFact(
              Seq(
                Ref(13, "03-04")
              )
            )
          )
        )
      )
    }
  }

  ignore("integrityCheckFacts") {

    withDatabase { database =>

      new NetworkRepositoryImpl(database).save(
        newNetwork(
          1,
          Some(Country.be),
          NetworkType.hiking,
          "network-1",
          networkFacts = NetworkFacts(
            integrityCheckFailed = Some(
              NetworkIntegrityCheckFailed(
                2,
                Seq(
                  NodeIntegrityCheck("01", 101, 3, 2, failed = false),
                  NodeIntegrityCheck("02", 102, 3, 3, failed = true)
                )
              )
            )
          )
        )
      )

      new FactRepositoryImpl(database).integrityCheckFacts("be", "rwn", Couch.uiTimeout, stale = false) should equal(
        IntegrityCheckPage(
          "be",
          "rwn",
          Seq(
            NetworkIntegrityInfo(
              1,
              "network-1",
              NetworkIntegrityCheckFailed(
                2,
                Seq(
                  NodeIntegrityCheck("01", 101, 3, 2, failed = false),
                  NodeIntegrityCheck("02", 102, 3, 3, failed = true)
                )
              )
            )
          )
        )
      )
    }
  }
}
