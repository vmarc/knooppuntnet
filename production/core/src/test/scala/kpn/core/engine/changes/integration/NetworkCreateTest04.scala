package kpn.core.engine.changes.integration

import kpn.core.analysis.Network
import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.NetworkChanges
import kpn.shared.NetworkExtraMemberWay
import kpn.shared.NetworkFacts
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.common.Ref
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.RefDiffs

class NetworkCreateTest04 extends AbstractTest {

  test("network create - investigate flag is set when issue in added network") {

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .way(102)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11),
          newMember("way", 102)
        )
      )
      .data

    val tc = new TestConfig()

    tc.relationAfter(dataAfter, 1) // new network relation
    tc.relationBefore(dataAfter, 11) // the route existed before
    tc.nodesBefore(dataAfter, 1001, 1002) // the network nodes existed before

    tc.process(ChangeAction.Create, TestData.relation(dataAfter, 1))

    tc.analysisData.networks.watched.contains(1) should equal(true)

    (tc.analysisRepository.saveNetwork _).verify(
      where { network: Network =>
        network.id should equal(1)
        network.facts should equal(
          NetworkFacts(
            networkExtraMemberWay = Some(
              Seq(
                NetworkExtraMemberWay(102)
              )
            )
          )
        )
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              creates = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "name",
                  routeChanges = ChangeSetElementRefs(
                    added = Seq(
                      ChangeSetElementRef(11, "01-02", happy = true, investigate = false)
                    )
                  ),
                  nodeChanges = ChangeSetElementRefs(
                    added = Seq(
                      ChangeSetElementRef(1001, "01", happy = true, investigate = false),
                      ChangeSetElementRef(1002, "02", happy = true, investigate = false)
                    )
                  ),
                  happy = true,
                  investigate = true
                )
              )
            ),
            happy = true,
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Create,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            networkNodes = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
            routes = RefDiffs(added = Seq(Ref(11, "01-02"))),
            ways = IdDiffs(
              added = Seq(
                102
              )
            ),
            happy = true,
            investigate = true
          )
        )
        true
      }
    )
  }
}
