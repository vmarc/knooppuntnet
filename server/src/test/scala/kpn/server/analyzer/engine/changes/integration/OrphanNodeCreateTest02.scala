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
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newNodeName

class OrphanNodeCreateTest02 extends IntegrationTest {

  test("create proposed orphan node") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData().node(
      1001,
      version = 1,
      tags = Tags.from(
        "proposed:rwn_ref" -> "01",
        "network:type" ->
          "node_network"
      )
    )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      watched.nodes.ids should contain(1001)

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
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("nl")
        ),
        country = Some(Country.nl),
        name = Some("01"),
        names = Seq(
          newNodeName(
            RouteType.hiking,
            RouteScope.regional,
            "01",
            proposed = true
          )
        ),
        version = 1,
        tags = Tags.from(
          "proposed:rwn_ref" -> "01",
          "network:type" -> "node_network"
        ),
        locations = Seq("nl"),
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
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
            "proposed:rwn_ref" -> "01",
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
      findChangeSetSummaryById("123:1"),
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
