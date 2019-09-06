package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetAnalysis
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.Timestamp
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.MapBounds
import kpn.shared.common.Ref
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.common.TrackSegmentFragment
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.diff.NetworkData
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs
import kpn.shared.route.Both
import kpn.shared.route.RouteInfo
import kpn.shared.route.RouteInfoAnalysis
import kpn.shared.route.RouteMap
import kpn.shared.route.RouteMemberInfo
import kpn.shared.route.RouteNetworkNodeInfo

class NetworkUpdateRouteTest01 extends AbstractTest {

  test("network update - route that is no longer part of the network after update, becomes orphan route if also not referenced in any other network") {

    val dataBefore = TestData2()
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
      .data

    val dataAfter = TestData2()
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
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.watchNetwork(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)
    tc.relationAfter(dataAfter, 11)

    // before:
    tc.analysisData.networks.watched.isReferencingRelation(11) should equal(true)
    tc.analysisData.orphanRoutes.watched.contains(11) should equal(false)

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    tc.analysisData.networks.watched.isReferencingRelation(11) should equal(false)
    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true)

    (tc.analysisRepository.saveNetwork _).verify(*).once()
    (tc.analysisRepository.saveNode _).verify(*).never()

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo should equal(
          newRouteInfo(
            newRouteSummary(
              11,
              name = "01-02",
              country = Some(Country.nl),
              wayCount = 1,
              nodeNames = Seq("01", "02"),
              tags = newRouteTags("01-02")
            ),
            orphan = true,
            tags = newRouteTags("01-02"),
            analysis = Some(
              RouteInfoAnalysis(
                Seq(
                  RouteNetworkNodeInfo(1001, "01", "01", "0", "0"))
                ,
                Seq(
                  RouteNetworkNodeInfo(1002, "02", "02", "0", "0")
                ),
                Seq(),
                Seq(),
                Seq(),
                Seq(
                  RouteMemberInfo(
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
                    isAccessible = true,
                    "0 m",
                    "2",
                    "",
                    Both,
                    Tags.empty
                  )
                ),
                "01-02",
                RouteMap(
                  MapBounds("0.0", "0.0", "0.0", "0.0"),
                  Some(TrackPath(1001, 1002, 0, Seq(TrackSegment("paved", TrackPoint("0", "0"), Seq(TrackSegmentFragment(TrackPoint("0", "0"), 0, 90, None)))))),
                  Some(TrackPath(1002, 1001, 0, Seq(TrackSegment("paved", TrackPoint("0", "0"), Seq(TrackSegmentFragment(TrackPoint("0", "0"), 0, 90, None)))))),
                  Seq(),
                  Seq(),
                  Seq(),
                  None,
                  None,
                  Seq(
                    RouteNetworkNodeInfo(1001, "01", "01", "0", "0")
                  ),
                  Seq(
                    RouteNetworkNodeInfo(1002, "02", "02", "0", "0")
                  ),
                  Seq(),
                  Seq(),
                  Seq()
                ),
                Seq(
                  "forward=(01-02 via +<01-02 101>)",
                  "backward=(02-01 via -<01-02 101>)"
                )
              )
            )
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
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
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            orphanRoutes = RefChanges(
              Seq(),
              Seq(Ref(11, "01-02")
              )
            ),
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None),
                      RawMember("node", 1002, None),
                      RawMember("relation", 11, None)
                    ),
                    tags = Tags.from("network" -> "rwn", "type" -> "network", "name" -> "name")
                  ),
                  "name"
                ),
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None),
                      RawMember("node", 1002, None)
                    ),
                    tags = Tags.from("network" -> "rwn", "type" -> "network", "name" -> "name")
                  ),
                  "name"
                )
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
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>

        val routeData = newRouteData(
          Some(Country.nl),
          NetworkType.hiking,
          newRawRelation(
            11,
            members = Seq(
              RawMember("way", 101, None)
            ),
            tags = newRouteTags("01-02")
          ),
          "01-02",
          networkNodes = Seq(
            newRawNode(1001, tags = newNodeTags("01")),
            newRawNode(1002, tags = newNodeTags("02"))
          ),
          nodes = Seq(
            newRawNode(
              1001,
              tags = newNodeTags("01")
            ),
            newRawNode(
              1002,
              tags = newNodeTags("02")
            )
          ),
          ways = Seq(
            newRawWay(
              101,
              nodeIds = Seq(1001, 1002),
              tags = Tags.from("highway" -> "unclassified")
            )
          )
        )

        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Update,
            "01-02",
            removedFromNetwork = Seq(Ref(1, "name")),
            before = Some(routeData),
            after = Some(routeData),
            facts = Seq(Fact.BecomeOrphan),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
