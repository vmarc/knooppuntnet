package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.NetworkType
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.ElementIds

class NetworkDelete_Test01 extends AbstractTest {

  test("network delete") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData().networkRelation(1, "network1")

      val tc = new TestContext(database, dataBefore, OverpassData.empty)

      tc.analysisContext.data.networks.watched.add(1, ElementIds())

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.data.networks.watched.contains(1))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(
        where { networkInfo: NetworkInfo =>
          assert(!networkInfo.active)
          true
        }
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
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
          investigate = true
        )
      )

      // TODO add delete test where before network contains nodes and routes that become orphan because of the delete
      //    orphanRoutes = RefChanges(newRefs = newOrphanRoutes),
      //    ignoredRoutes = RefChanges(newRefs = newIgnoredRoutes),
      //    orphanNodes = RefChanges(newRefs = newOrphanNodes),
      //    ignoredNodes = RefChanges(newRefs = newIgnoredNodes),

      val networkChange = tc.findNetworkChangeById("123:1:1")
      networkChange.key.changeSetId should equal(123)
      networkChange.key.elementId should equal(1)
      networkChange.changeType should equal(ChangeType.Delete)
      networkChange.networkType should equal(NetworkType.hiking)
      networkChange.networkName should equal("network1")
      assert(!networkChange.happy)
      assert(networkChange.investigate)
    }
  }
}
