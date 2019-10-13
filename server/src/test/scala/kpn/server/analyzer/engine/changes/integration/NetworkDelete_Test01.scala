package kpn.server.analyzer.engine.changes.integration

import kpn.server.analyzer.engine.changes.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetNetwork
import kpn.shared.ChangeSetSummary
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.network.NetworkInfo

class NetworkDelete_Test01 extends AbstractTest {

  test("network delete") {

    val data = TestData2()
      .networkRelation(1, "network1")
      .data
    val tc = new TestConfig()
    tc.relationBefore(data, 1)

    tc.analysisContext.data.networks.watched.add(1, ElementIds())

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisContext.data.networks.watched.contains(1) should equal(false)

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo.active should equal(false)
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          ChangeSetSummary(
            key = ChangeKey(1, Timestamp(2015, 8, 11, 0, 0, 0), 123, 0),
            subsets = Seq.empty,
            timestampFrom = Timestamp(2015, 8, 11, 0, 0, 2),
            timestampUntil = Timestamp(2015, 8, 11, 0, 0, 3),
            networkChanges = NetworkChanges(
              deletes = Seq(
                ChangeSetNetwork(
                  country = None, // TODO change test data so that no 'None' here
                  networkType = NetworkType.hiking,
                  networkId = 1,
                  networkName = "network1",
                  routeChanges = ChangeSetElementRefs(),
                  nodeChanges = ChangeSetElementRefs(),
                  happy = false,
                  investigate = true
                )
              )
            ),
            orphanRouteChanges = Seq.empty,
            orphanNodeChanges = Seq.empty,
            happy = false,
            investigate = true
          )
        )
        true
      }
    )

    // TODO add delete test where before network contains nodes and routes that become orphan because of the delete
    //    orphanRoutes = RefChanges(newRefs = newOrphanRoutes),
    //    ignoredRoutes = RefChanges(newRefs = newIgnoredRoutes),
    //    orphanNodes = RefChanges(newRefs = newOrphanNodes),
    //    ignoredNodes = RefChanges(newRefs = newIgnoredNodes),

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange.key.changeSetId should equal(123)
        networkChange.key.elementId should equal(1)
        networkChange.changeType should equal(ChangeType.Delete)
        networkChange.networkType should equal(NetworkType.hiking)
        networkChange.networkName should equal("network1")
        networkChange.happy should equal(false)
        networkChange.investigate should equal(true)
        true
      }
    )
  }

}
