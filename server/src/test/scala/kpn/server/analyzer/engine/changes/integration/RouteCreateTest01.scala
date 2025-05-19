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
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.MemberType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class RouteCreateTest01 extends IntegrationTest {

  test("create route") {

    val dataBefore = OverpassData.empty

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        ChangeAction.Create,
        dataAfter.rawNodeWithId(1001),
        dataAfter.rawNodeWithId(1002),
        dataAfter.rawWayWithId(101),
        dataAfter.rawRelationWithId(11)
      )

      watched.routes.ids should contain(11)
      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)

      assertBaseRoute()
      // TODO redesign - assertBaseNode1001()
      // TODO redesign - assertBaseNode1002()
      assertRoute()
      assertOrphanRoute()
      assertNode1001()
      assertNode1002()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()

      database.orphanNodes shouldBe empty
    }
  }

  private def assertBaseRoute(): Unit = {
    val baseRouteDoc = findBaseRouteById(11L)
    baseRouteDoc.summary.name should equal("01-02")
    // TODO redesign - add detailed doc comparison?
  }

  private def assertRoute(): Unit = {
    val routeDoc = findRouteById(11L)
    routeDoc.summary.name should equal("01-02")
    // TODO redesign - add detailed doc comparison?
  }

  private def assertOrphanRoute(): Unit = {
    assertEqual(
      findOrphanRouteById(11L),
      newOrphanRouteDoc(
        11L,
        country = Country.nl,
        routeType = RouteType.hiking,
        name = "01-02"
      )
    )
  }

  private def assertNode1001(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        labels = Seq(
          Label.active,
          Label.routeType(RouteType.hiking)
        ),
        country = Some(Country.nl),
        name = Some("01"),
        names = Seq(
          newNodeName(
            RouteType.hiking,
            RouteScope.regional,
            "01"
          )
        ),
        tags = newNodeTags("01"),
        routeReferences = Seq(
          Reference(RouteType.hiking, RouteScope.regional, 11, "01-02", None)
        )
      )
    )
  }

  private def assertNode1002(): Unit = {
    assertEqual(
      findNodeById(1002),
      newNodeDoc(
        1002,
        labels = Seq(
          Label.active,
          Label.routeType(RouteType.hiking)
        ),
        country = Some(Country.nl),
        name = Some("02"),
        names = Seq(
          newNodeName(
            RouteType.hiking,
            RouteScope.regional,
            "02"
          )
        ),
        tags = newNodeTags("02"),
        routeReferences = Seq(
          Reference(RouteType.hiking, RouteScope.regional, 11, "01-02", None)
        )
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
        after = Some(
          newRouteData(
            relationId = 11,
            meta = newMetaData(changeSetId = 1),
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
              "network:type" -> "node_network"
            )
          )
        ),
        addedWays = Seq(
          newRawWay(
            id = 101,
            nodeIds = Vector(
              1001,
              1002
            ),
            tags = Tags.from(
              "highway" -> "unclassified"
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
        newChangeKey(elementId = 1001),
        ChangeType.Create,
        Seq(Subset.nlHiking),
        name = Some("01"),
        before = None,
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        initialTags = Some(
          Tags.from(
            "rwn_ref" -> "01",
            "network:type" -> "node_network"
          )
        ),
        initialLatLon = Some(
          LatLonImpl("0", "0")
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
        newChangeKey(elementId = 1002),
        ChangeType.Create,
        Seq(Subset.nlHiking),
        name = Some("02"),
        before = None,
        after = Some(
          newMetaData()
        ),
        addedToRoute = Seq(
          Ref(11, "01-02")
        ),
        initialTags = Some(
          Tags.from(
            "rwn_ref" -> "02",
            "network:type" -> "node_network"
          )
        ),
        initialLatLon = Some(
          LatLonImpl("0", "0")
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
        orphanRouteChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(newChangeSetElementRef(11, "01-02", happy = true))
            )
          )
        ),
        orphanNodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(
                newChangeSetElementRef(1001, "01", happy = true),
                newChangeSetElementRef(1002, "02", happy = true)
              )
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
