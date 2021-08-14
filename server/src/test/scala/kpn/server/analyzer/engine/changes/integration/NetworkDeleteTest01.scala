package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.custom.NetworkType
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkDeleteTest01 extends AbstractIntegrationTest {

  test("network delete") {

    val dataBefore = OverpassData().networkRelation(1, "network1")
    val dataAfter = OverpassData.empty

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.watched.networks.contains(1))

      assertNetwork(tc)
      assertNetworkInfo(tc)

      // TODO add delete test where before network contains nodes and routes that become orphan because of the delete
      //    orphanRoutes = RefChanges(newRefs = newOrphanRoutes),
      //    ignoredRoutes = RefChanges(newRefs = newIgnoredRoutes),
      //    orphanNodes = RefChanges(newRefs = newOrphanNodes),
      //    ignoredNodes = RefChanges(newRefs = newIgnoredNodes),

      assertNetworkChange(tc)
      assertChangeSetSummary(tc)
    }
  }

  private def assertNetwork(tc: IntegrationTestContext): Unit = {
    tc.findNetworkById(1) should matchTo(
      newNetwork(
        1L,
        active = false,
        tags = Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network1",
        )
      )
    )
  }

  private def assertNetworkInfo(tc: IntegrationTestContext): Unit = {
    tc.findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1L,
        active = false,
        country = None, // TODO !!
        summary = newNetworkSummary(
          name = "network1"
        ),
        detail = newNetworkDetail(
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network1",
          )
        )
      )
    )
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
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
  }

  private def assertNetworkChange(tc: IntegrationTestContext): Unit = {
    val networkChange = tc.findNetworkInfoChangeById("123:1:1")
    networkChange.key.changeSetId should equal(123)
    networkChange.key.elementId should equal(1)
    networkChange.changeType should equal(ChangeType.Delete)
    networkChange.networkType should equal(NetworkType.hiking)
    networkChange.networkName should equal("network1")
    assert(!networkChange.happy)
    assert(networkChange.investigate)
  }
}
