package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NetworkChanges
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.data.MemberType
import kpn.api.common.data.MetaData
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.Timestamps

class NetworkCreateTest07 extends IntegrationTest {

  test("network create - containing existing route with update") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        ),
        tags = Tags.from("newkey" -> "value") // modify
      )
      .networkRelation( // create
        1,
        "name",
        Seq(
          newMember(MemberType.Relation, 11)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        Seq(
          Change(
            ChangeAction.Modify,
            Seq(
              dataAfter.rawRelationWithId(11)

            )
          ),
          Change(
            ChangeAction.Create,
            Seq(
              dataAfter.rawRelationWithId(1)
            )
          ),
        )
      )

      watched.networks.ids should contain(1)

      database.nodeChanges shouldBe empty

      assertNetwork()
      assertNetworkChange()
      assertRouteChange()
      assertChangeSetSummary()
    }
  }

  private def assertNetwork(): Unit = {
    val networkDoc = findBaseNetworkById(1)
    networkDoc._id should equal(1)
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "name",
        changeType = ChangeType.Create,
        country = Option(Country.nl),
        routeType = RouteType.hiking,
        networkDataUpdate = Some(
          NetworkDataUpdate(
            before = None,
            after = Some(
              NetworkData(
                MetaData(
                  version = 0,
                  timestamp = Timestamp(2015, 8, 11),
                  changeSetId = 1
                ),
                "name"
              )
            )
          )
        ),
        relations = IdDiffs(added = Seq(11)),
        nodeDiffs = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
        routeDiffs = RefDiffs(added = Seq(Ref(11, "01-02"))),
        happy = true,
        impact = true,
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("123:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        addedToNetwork = Seq(Ref(1, "name")),
        before = Some(
          newRouteData(
            relationId = 11,
            meta = MetaData(0, Timestamps.default, 1),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network",
            ),
          )
        ),
        after = Some(
          newRouteData(
            relationId = 11,
            meta = MetaData(0, Timestamps.default, 1),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
            tags = Tags.from(
              "network" -> "rwn",
              "type" -> "route",
              "route" -> "foot",
              "ref" -> "01-02",
              "network:type" -> "node_network",
              "newkey" -> "value",
            ),
          )
        ),
        diffs = RouteDiff(
          tagDiffs = Some(
            TagDiffs(
              mainTags = Seq(
                TagDiff.same("ref", "01-02"),
                TagDiff.same("network", "rwn"),
                TagDiff.same("type", "route"),
                TagDiff.same("route", "foot"),
                TagDiff.same("network:type", "node_network")
              ),
              extraTags = Seq(
                TagDiff.add("newkey", "value")
              )
            )
          )
        ),
        nodeChanges = Seq(
          newRouteNodeChange(
            1001,
          ),
          newRouteNodeChange(
            1002,
          )
        ),
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
        networkChanges = NetworkChanges(
          creates = Seq(
            newChangeSetNetwork(
              Some(Country.nl),
              RouteType.hiking,
              1,
              "name",
              routeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(11, "01-02", happy = true, investigate = false)
                )
              ),
              nodeChanges = ChangeSetElementRefs(
                added = Seq(
                  ChangeSetElementRef(1001, "01", happy = true, investigate = false),
                  ChangeSetElementRef(1002, "02", happy = true, investigate = false)
                )
              ),
              happy = true
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
