package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.LatLonImpl
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class NetworkAddRouteTest01 extends IntegrationTest {

  test("network add route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(
        1,
        "network",
        version = 1,
        members = Seq(
          newMember("node", 1001),
          newMember("node", 1002)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(
        1,
        "network",
        version = 2,
        members = Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("relation", 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assertNetworkInfo()
      assertNetworkInfoChange()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()
      assertNodeRouteReferences()
    }
  }

  private def assertNetworkInfo(): Unit = {
    findNetworkInfoById(1).shouldMatchTo(
      newNetworkInfoDoc(
        1,
        country = Some(Country.nl),
        summary = newNetworkSummary(
          name = "network",
          nodeCount = 2,
          routeCount = 1,
          changeCount = 1
        ),
        detail = newNetworkDetail(
          version = 2,
          tags = Tags.from(
            "network:type" -> "node_network",
            "type" -> "network",
            "network" -> "rwn",
            "name" -> "network",
          ),
          center = Some(LatLonImpl("0.0", "0.0")),
        ),
        nodes = Seq(
          newNetworkInfoNodeDetail(
            1001,
            "01",
            definedInRelation = true,
          ),
          newNetworkInfoNodeDetail(
            1002,
            "02",
            definedInRelation = true,
          )
        ),
        routes = Seq(
          newNetworkInfoRouteDetail(
            11,
            "01-02",
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "note" -> "01-02",
              "network:type" -> "node_network"
            ),
            nodeRefs = Seq(
              1001,
              1002)
          )
        )
      )
    )
  }

  private def assertNetworkInfoChange(): Unit = {
    findNetworkInfoChangeById("123:1:1").shouldMatchTo(
      newNetworkInfoChange(
        newChangeKey(elementId = 1),
        ChangeType.Update,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "network",
        networkDataUpdate = Some(
          NetworkDataUpdate(
            Some(
              NetworkData(
                MetaData(1, defaultTimestamp, 1),
                "network"
              )
            ),
            Some(
              NetworkData(
                MetaData(2, defaultTimestamp, 1),
                "network"
              )
            )
          )
        ),
        nodeDiffs = RefDiffs(
          updated = Seq(
            Ref(1001, "01"),
            Ref(1002, "02"),
          )
        ),
        routeDiffs = RefDiffs(
          added = Seq(
            Ref(11, "01-02")
          )
        ),
        happy = true
      )
    )
  }

  private def assertRouteChange(): Unit = {

    val routeData = newRouteData(
      Some(Country.nl),
      NetworkType.hiking,
      relation = newRawRelation(
        11,
        members = Seq(
          RawMember("way", 101, None)
        ),
        tags = newRouteTags("01-02")
      ),
      name = "01-02",
      networkNodes = Seq(
        newRawNodeWithName(1001, "01"),
        newRawNodeWithName(1002, "02")
      ),
      nodes = Seq(
        newRawNodeWithName(1001, "01"),
        newRawNodeWithName(1002, "02")
      ),
      ways = Seq(
        newRawWay(
          101,
          nodeIds = Seq(1001, 1002),
          tags = Tags.from("highway" -> "unclassified")
        )
      )
    )

    findRouteChangeById("123:1:11").shouldMatchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network")),
        before = None,
        after = Some(routeData),
        impactedNodeIds = Seq(1001, 1002),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(): Unit = {
    findNodeChangeById("123:1:1001").shouldMatchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    findNodeChangeById("123:1:1002").shouldMatchTo(
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
        addedToRoute = Seq(Ref(11, "01-02")),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1").shouldMatchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        networkChanges = NetworkChanges(
          updates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              NetworkType.hiking,
              1,
              "network",
              nodeChanges = ChangeSetElementRefs(
                updated = Seq(
                  ChangeSetElementRef(
                    1001,
                    "01",
                    happy = true,
                    investigate = false
                  ),
                  ChangeSetElementRef(
                    1002,
                    "02",
                    happy = true,
                    investigate = false
                  )
                )
              ),
              routeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(
                    11,
                    "01-02",
                    happy = true,
                    investigate = false
                  )
                )
              ),
              happy = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(
            Subset.nlHiking,
            happy = true
          )
        ),
        happy = true
      )
    )
  }

  private def assertNodeRouteReferences(): Unit = {
    val nodeRouteReferences = Seq(
      Reference(
        NetworkType.hiking,
        NetworkScope.regional,
        11,
        "01-02"
      )
    )
    context.nodeRepository.nodeRouteReferences(1001) should equal(nodeRouteReferences)
    context.nodeRepository.nodeRouteReferences(1002) should equal(nodeRouteReferences)
  }
}
