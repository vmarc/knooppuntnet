package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class OrphanNodeCreateTest01 extends IntegrationTest {

  test("create orphan node") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData()
      .networkNode(1001, "01", version = 1)

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(watched.nodes.contains(1001))

      assertNode()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNode() = {
    findNodeById(1001) should matchTo(
      newNodeDoc(
        1001,
        labels = Seq(
          "active",
          "network-type-hiking"
        ),
        country = Some(Country.nl),
        name = "01",
        names = Seq(
          newNodeName(
            NetworkType.hiking,
            NetworkScope.regional,
            "01"
          )
        ),
        version = 1,
        lastUpdated = Timestamp(2015, 8, 11, 0, 0, 0),
        tags = newNodeTags("01")
      )
    )
  }

  private def assertChangeSetSummary() = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(newChangeSetElementRef(1001, "01", happy = true))
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

  private def assertNodeChange() = {
    findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Create,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        after = Some(
          newMetaData(version = 1)
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }
}
