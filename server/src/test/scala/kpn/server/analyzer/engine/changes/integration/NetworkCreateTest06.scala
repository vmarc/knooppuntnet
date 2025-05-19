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
import kpn.api.common.diff.WayDiffs
import kpn.api.custom.Change
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData

class NetworkCreateTest06 extends IntegrationTest {

  test("network create - with new route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02", Tags.from("tag" -> "after")) // modify
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember(MemberType.Way, 101))) // create
      .networkRelation(1, "network-name", Seq(newMember(MemberType.Relation, 11))) // create

    testIntegration(dataBefore, dataAfter) {

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)

      process(
        Seq(
          Change(
            ChangeAction.Modify,
            Seq(
              dataAfter.rawNodeWithId(1002),
            )
          ),
          Change(
            ChangeAction.Create,
            Seq(
              dataAfter.rawRelationWithId(1),
              dataAfter.rawRelationWithId(11),
            )
          )
        )
      )

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.routes.ids should contain(11)
      watched.networks.ids should contain(1)

      assertBaseNetwork()
      assertNetwork()
      assertNetworkChange()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()
    }
  }

  private def assertBaseNetwork(): Unit = {
    val baseNetworkDoc = findBaseNetworkById(1)
    baseNetworkDoc._id should equal(1)
  }

  private def assertNetwork(): Unit = {
    val networkInfoDoc = findNetworkById(1)
    networkInfoDoc._id should equal(1)
    //  networkInfoDoc.detail.networkFacts.shouldMatchTo(
    //    NetworkFacts(
    //      networkExtraMemberWay = Some(
    //        Seq(
    //          NetworkExtraMemberWay(102)
    //        )
    //      )
    //    )
    //  )
  }

  private def assertNetworkChange(): Unit = {
    assertEqual(
      findNetworkChangeById("123:1:1"),
      newNetworkChange(
        key = newChangeKey(elementId = 1),
        networkName = "network-name",
        changeType = ChangeType.Create,
        country = Some(Country.nl),
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
                "network-name"
              )
            )
          ),
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
        ChangeType.Create,
        "01-02",
        addedToNetwork = Seq(Ref(1, "network-name")),
        before = None,
        after = Some(
          newRouteData(
            relationId = 11,
            meta = MetaData(0, defaultTimestamp, 1),
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
        wayDiffs = WayDiffs(
          added = Seq(
            newRawWay(
              101,
              nodeIds = Vector(1001, 1002),
              tags = Tags.from(
                "highway" -> "unclassified"
              )
            )
          )
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1001(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("01"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }

  private def assertNodeChange1002(): Unit = {
    assertEqual(
      findNodeChangeById("123:1:1002"),
      newNodeChange(
        key = newChangeKey(elementId = 1002),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        name = Some("02"),
        before = Some(
          newMetaData()
        ),
        after = Some(
          newMetaData()
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDiff.same("rwn_ref", "02"),
              TagDiff.same("network:type", "node_network")
            ),
            extraTags = Seq(
              TagDiff.add("tag", "after")
            )
          )
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
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
              "network-name",
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
