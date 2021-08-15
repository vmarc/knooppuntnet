package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.MapBounds
import kpn.api.common.common.Ref
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.RouteData
import kpn.api.common.route.Both
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkUpdateRouteTest01 extends IntegrationTest {

  test("network update - route that is no longer part of the network after update, becomes orphan route if also not referenced in any other network") {

    pending

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("relation", 11)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // route still exists
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002)
          // route member is no longer included here
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      assert(watched.routes.contains(11))

      assertRoute()
      assertNetworkInfoChange()
      assertRouteChange()
      assertChangeSetSummary()

      assert(database.nodeChanges.isEmpty)
    }
  }

  private def assertRoute(): Unit = {
    findRouteById(11) should matchTo(
      newRouteInfo(
        newRouteSummary(
          11,
          name = "01-02",
          country = Some(Country.nl),
          wayCount = 1,
          nodeNames = Seq("01", "02"),
          tags = newRouteTags("01-02")
        ),
        labels = Seq(
          "active",
          "network-type-hiking"
        ),
        orphan = true,
        tags = newRouteTags("01-02"),
        analysis = newRouteInfoAnalysis(
          members = Seq(
            kpn.api.custom.RouteMemberInfo(
              101,
              "way",
              isWay = true,
              Seq(
                RouteNetworkNodeInfo(1001, "01", "01", "0", "0"),
                RouteNetworkNodeInfo(1002, "02", "02", "0", "0")
              ),
              "wn003",
              "1",
              1002,
              "2",
              1001,
              "",
              Timestamp(2015, 8, 11, 0, 0, 0),
              accessible = true,
              "0 m",
              "2",
              "",
              Both,
              Tags.empty
            )
          ),
          expectedName = "01-02",
          map = newRouteMap(
            bounds = MapBounds("0.0", "0.0", "0.0", "0.0"),
            forwardPath = Some(
              TrackPath(
                pathId = 1,
                startNodeId = 1001,
                endNodeId = 1002,
                meters = 0,
                oneWay = false,
                segments = Seq(
                  TrackSegment(
                    "paved",
                    TrackPoint("0", "0"),
                    Seq(TrackSegmentFragment(TrackPoint("0", "0"),
                      0,
                      90,
                      None)
                    )
                  )
                )
              )
            ),
            backwardPath = Some(
              TrackPath(
                pathId = 2,
                startNodeId = 1002,
                endNodeId = 1001,
                meters = 0,
                oneWay = false,
                segments = Seq(
                  TrackSegment(
                    "paved",
                    TrackPoint("0", "0"),
                    Seq(
                      TrackSegmentFragment(TrackPoint("0", "0"), 0, 90, None)
                    )
                  )
                )
              )
            ),
            startNodes = Seq(
              RouteNetworkNodeInfo(1001, "01", "01", "0", "0")
            ),
            endNodes = Seq(
              RouteNetworkNodeInfo(1002, "02", "02", "0", "0")
            )
          ),
          structureStrings = Seq(
            "forward=(01-02 via +<01-02 101>)",
            "backward=(02-01 via -<01-02 101>)"
          ),
          geometryDigest = "39dfa55283318d31afe5a3ff4a0e3253e2045e43"
        ),
        nodeRefs = Seq(
          1001,
          1002
        )
      )
    )
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
        orphanRoutes = RefChanges(
          Seq.empty,
          Seq(Ref(11, "01-02")
          )
        ),
        networkDataUpdate = Some(
          NetworkDataUpdate(
            newNetworkData(name = "name"),
            newNetworkData(name = "name")
          )
        ),
        networkNodes = RefDiffs(
          updated = Seq(
            Ref(1001, "01"),
            Ref(1002, "02")
          )
        ),
        routes = RefDiffs(
          removed = Seq(
            Ref(11, "01-02")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertRouteChange() = {

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

    findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "name")),
        before = Some(routeData),
        after = Some(routeData),
        facts = Seq(Fact.BecomeOrphan),
        investigate = true,
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
              routeChanges = ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(11, "01-02", investigate = true)
                )
              ),
              nodeChanges = ChangeSetElementRefs(
                updated = Seq(
                  newChangeSetElementRef(1001, "01"),
                  newChangeSetElementRef(1002, "02")
                )
              ),
              investigate = true
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
        ),
        investigate = true
      )
    )
  }
}
