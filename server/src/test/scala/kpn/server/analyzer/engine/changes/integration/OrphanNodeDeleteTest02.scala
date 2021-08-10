package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Reference
import kpn.api.common.node.NodeIntegrity
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest02 extends AbstractTest {

  test("delete orphan node, 'before' not in overpass, but NodeDoc in mongodb") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)
      tc.analysisContext.data.nodes.watched.add(1001)
      setupNodeDoc(tc)

      tc.process(ChangeAction.Delete, newRawNode(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      tc.findNodeById(1001) should matchTo(
        newNodeDoc(
          1001,
          labels = Seq.empty,
          active = false,
          Some(Country.nl),
          "01",
          names = Seq(
            newNodeName(
              NetworkType.hiking,
              NetworkScope.regional,
              "01"
            )
          ),
          locations = Seq(
            "location-1",
            "location-2"
          ),
          tiles = Seq.empty,
          facts = Seq.empty,
          integrity = None,
          routeReferences = Seq.empty
        )
      )

      tc.findChangeSetSummaryById("123:1") should matchTo {
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          orphanNodeChanges = Seq(
            ChangeSetSubsetElementRefs(
              Subset.nlHiking,
              ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
                )
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
          ),
          investigate = true
        )
      }

      tc.findNodeChangeById("123:1:1001") should matchTo {
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Delete,
          subsets = Seq(Subset.nlHiking),
          locations = Seq("location-1", "location-2"),
          name = "01",
          before = None,
          facts = Seq(Fact.Deleted),
          investigate = true,
          impact = true,
          locationInvestigate = true,
          locationImpact = true
        )
      }
    }
  }

  private def setupNodeDoc(tc: TestContext): Unit = {
    tc.nodeRepository.save(
      newNodeDoc(
        1001,
        labels = Seq(
          "active",
          "facts",
          "fact-IntegrityCheckFailed" // <-- this fact will be removed
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
        locations = Seq(
          "location-1",
          "location-2"
        ),
        tiles = Seq(
          "tile-1",
          "tile-2"
        ),
        integrity = Some(
          NodeIntegrity()
        ),
        routeReferences = Seq(
          Reference(
            NetworkType.hiking,
            NetworkScope.regional,
            11,
            "01-02"
          )
        )
      )
    )
  }
}
