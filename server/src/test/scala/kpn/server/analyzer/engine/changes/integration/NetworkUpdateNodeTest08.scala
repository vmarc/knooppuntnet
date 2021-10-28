package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData

class NetworkUpdateNodeTest08 extends IntegrationTest {

  test("network update - an orphan node that is added to the network is no longer orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02") // orphan node, not referenced by the network
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001)
          // the network does not reference the orphan node
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002) // reference to the previous orphan node
        )
      )

    testIntegration(dataBefore, dataAfter) {

      findOrphanNodeById("nl:hiking:1002") should matchTo(
        newOrphanNodeDoc(
          country = Country.nl,
          networkType = NetworkType.hiking,
          nodeId = 1002,
          name = "02"
        )
      )

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(database.orphanNodes.findAll().isEmpty)

      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))

      assertNetwork()
      assertNetworkInfo()
      assertNetworkInfoChange()
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkInfo(): Unit = {
    val networkInfoDoc = findNetworkInfoById(1)
    networkInfoDoc._id should equal(1)
  }

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1") should matchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "name",
        networkDataUpdate = None,
        nodeDiffs = RefDiffs(
          added = Seq(Ref(1002, "02"))
        ),
        happy = true
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    findNodeChangeById("123:1:1002") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "02",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToNetwork = Seq(Ref(1, "name")),
        happy = true,
        impact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
              1,
              "name",
              nodeChanges = ChangeSetElementRefs(
                added = Seq(newChangeSetElementRef(1002, "02", happy = true))
              ),
              happy = true
            )
          )
        ),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1002, "02", happy = true)
              )
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
        ),
        happy = true
      )
    )
  }
}
