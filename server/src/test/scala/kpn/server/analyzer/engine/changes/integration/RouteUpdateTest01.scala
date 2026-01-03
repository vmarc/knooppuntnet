package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteChange
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newOrphanRouteInfo
import kpn.core.test.TestObjects.newRaw
import kpn.core.test.TestObjects.newRouteChange
import kpn.core.test.TestObjects.newRouteData
import kpn.core.test.TestObjects.newRouteDiff
import kpn.core.test.TestObjects.newRouteNode
import kpn.core.test.TestObjects.newRouteNodeChange

class RouteUpdateTest01 extends IntegrationTest {

  test("update route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        ),
        Tags.from("key" -> "value1"),
        1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        ),
        Tags.from("key" -> "value2"),
        2
      )

    testIntegration(dataBefore, dataAfter) {

      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(11))

      assert(watched.routes.contains(11))

      assertRoute()
      assertBaseRouteChange()
      assertRouteChange()
      assertOrphanRoute()
      database.nodeChanges shouldBe empty
      assertChangeSetSummary()
    }
  }

  private def assertRoute(): Unit = {
    val routeDoc = findRouteById(11)
    routeDoc._id should equal(11)
    assert(routeDoc.active)
  }

  private def assertBaseRouteChange(): Unit = {
    assertEqual(
      findBaseRouteChangeById("1:1:11"),
      newBaseRouteChange(
        "1:1:11",
        newChangeKey(elementId = 11),
        ChangeType.Update,
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        routeDiff = newRouteDiff(
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
                TagDiff.update("key", "value1", "value2")
              )
            )
          )
        )
      )
    )
  }

  private def assertRouteChange(): Unit = {
    assertEqual(
      findRouteChangeById("1:1:11"),
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        before = Some(
          newRouteData(
            relationId = 11,
            raw = newRaw(
              version = 1,
              tags = Tags.from(
                "network" -> "rwn",
                "type" -> "route",
                "route" -> "foot",
                "ref" -> "01-02",
                "network:type" -> "node_network",
                "key" -> "value1" // <--
              )
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
          )
        ),
        after = Some(
          newRouteData(
            relationId = 11,
            raw = newRaw(
              version = 2,
              tags = Tags.from(
                "network" -> "rwn",
                "type" -> "route",
                "route" -> "foot",
                "ref" -> "01-02",
                "network:type" -> "node_network",
                "key" -> "value2" // <--
              )
            ),
            countries = Seq(Country.nl),
            routeTypes = Seq(RouteType.hiking),
            name = "01-02",
            networkNodes = Seq(
              newRouteNode(1001, "01"),
              newRouteNode(1002, "02")
            ),
          )
        ),
        nodeChanges = Seq(
          newRouteNodeChange(1001),
          newRouteNodeChange(1002)
        ),
      )
    )
  }

  private def assertOrphanRoute(): Unit = {
    assertEqual(
      findOrphanRouteById(11L),
      newOrphanRouteInfo(
        11L,
        name = "01-02"
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(newChangeSetElementRef(11, "01-02"))
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking)
        )
      )
    )
  }
}
