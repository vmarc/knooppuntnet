package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.LatLonImpl
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName
import kpn.core.test.TestObjects.newNodeTags
import kpn.core.test.TestObjects.newRaw

class OrphanNodeCreateTest01 extends IntegrationTest {

  test("create orphan node") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData()
      .networkNode(1001, "01", version = 1)

    testIntegration(dataBefore, dataAfter) {

      processNode(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(watched.nodes.contains(1001))

      assertNode()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        base = newNodeBaseData(
          raw = newRaw(
            version = 1,
            timestamp = Timestamp(2015, 8, 11, 0, 0, 0),
            tags = newNodeTags("01")
          ),
          name = Some("01"),
          names = Seq(
            newNodeName(
              RouteType.hiking,
              RouteScope.regional,
              "01"
            )
          ),
          country = Some(Country.nl),
          locations = Seq("nl")
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("nl")
        )
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("1:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Create,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("01"),
        after = Some(
          newMetaData(version = 1)
        ),
        initialTags = Some(
          Tags.from(
            "rwn_ref" -> "01",
            "network:type" -> "node_network"
          )
        ),
        initialLatLon = Some(LatLonImpl("0", "0")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        orphanNodeChanges = Seq(
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
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              added = Seq(
                newChangeSetElementRef(1001, "01", happy = true),
              ),
            ),
            happy = true
          )
        ),
        happy = true
      )
    )
  }
}
