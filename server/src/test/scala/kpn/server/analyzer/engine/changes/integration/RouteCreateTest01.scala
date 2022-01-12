package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.LatLonImpl
import kpn.api.common.changes.ChangeAction
import kpn.api.common.common.Ref
import kpn.api.common.common.Reference
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
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
          newMember("way", 101)
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Create, dataAfter.rawRelationWithId(11))

      assert(watched.routes.contains(11))
      assert(watched.nodes.contains(1001))
      assert(watched.nodes.contains(1002))

      assertRoute()
      assertOrphanRoute()
      assertNode1001()
      assertNode1002()
      assertRouteChange()
      assertNodeChange1001()
      assertNodeChange1002()
      assertChangeSetSummary()

      assert(database.orphanNodes.isEmpty)
    }
  }

  private def assertRoute(): Unit = {
    val routeDoc = findRouteById(11L)
    routeDoc.summary.name should equal("01-02")
  }

  private def assertOrphanRoute(): Unit = {
    findOrphanRouteById(11L).shouldMatchTo(
      newOrphanRouteDoc(
        11L,
        country = Country.nl,
        networkType = NetworkType.hiking,
        name = "01-02"
      )
    )
  }

  private def assertNode1001(): Unit = {
    findNodeById(1001).shouldMatchTo {
      newNodeDoc(
        1001,
        labels = Seq(
          Label.active,
          Label.networkType(NetworkType.hiking)
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
        tags = newNodeTags("01"),
        routeReferences = Seq(
          Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02")
        )
      )
    }
  }

  private def assertNode1002(): Unit = {
    findNodeById(1002).shouldMatchTo {
      newNodeDoc(
        1002,
        labels = Seq(
          Label.active,
          Label.networkType(NetworkType.hiking)
        ),
        country = Some(Country.nl),
        name = "02",
        names = Seq(
          newNodeName(
            NetworkType.hiking,
            NetworkScope.regional,
            "02"
          )
        ),
        tags = newNodeTags("02"),
        routeReferences = Seq(
          Reference(NetworkType.hiking, NetworkScope.regional, 11, "01-02")
        )
      )
    }
  }

  private def assertRouteChange(): Unit = {
    findRouteChangeById("123:1:11").shouldMatchTo {
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Create,
        "01-02",
        after = Some(
          newRouteData(
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
        ),
        impactedNodeIds = Seq(1001, 1002),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    }
  }

  private def assertNodeChange1001(): Unit = {
    findNodeChangeById("123:1:1001").shouldMatchTo {
      newNodeChange(
        newChangeKey(elementId = 1001),
        ChangeType.Create,
        Seq(Subset.nlHiking),
        name = "01",
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
    }
  }

  private def assertNodeChange1002(): Unit = {
    findNodeChangeById("123:1:1002").shouldMatchTo {
      newNodeChange(
        newChangeKey(elementId = 1002),
        ChangeType.Create,
        Seq(Subset.nlHiking),
        name = "02",
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
    }
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1").shouldMatchTo {
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
    }
  }
}
