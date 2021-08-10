package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateNodeTest10 extends AbstractIntegrationTest {

  test("network update - node role 'connection' added") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .networkRelation(
          1,
          "network-name",
          Seq(
            newMember("node", 1001)
          )
        )

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkRelation(
          1,
          "network-name",
          Seq(
            newMember("node", 1001, "connection")
          )
        )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)
      tc.watchNetwork(tc.before, 1)

      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          networkChanges = NetworkChanges(
            updates = Seq(
              newChangeSetNetwork(
                Some(Country.nl),
                NetworkType.hiking,
                1,
                "network-name",
                nodeChanges = ChangeSetElementRefs(
                  updated = Seq(newChangeSetElementRef(1001, "01"))
                )
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking)
          )
        )
      )

      tc.findNetworkChangeById("123:1:1") should matchTo(
        newNetworkChange(
          newChangeKey(elementId = 1),
          ChangeType.Update,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "network-name",
          networkDataUpdate = Some(
            NetworkDataUpdate(
              newNetworkData(name = "network-name"),
              newNetworkData(name = "network-name")
            )
          ),
          networkNodes = RefDiffs(
            updated = Seq(
              Ref(1001, "01")
            )
          )
        )
      )

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "01",
          before = Some(
            newRawNodeWithName(1001, "01")
          ),
          after = Some(
            newRawNodeWithName(1001, "01")
          ),
          roleConnectionChanges = Seq(
            RefBooleanChange(Ref(1, "network-name"), after = true)
          )
        )
      )
    }
  }
}
