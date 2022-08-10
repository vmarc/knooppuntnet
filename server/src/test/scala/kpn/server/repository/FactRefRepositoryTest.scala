package kpn.server.repository

import kpn.api.common.NetworkFact
import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest
import kpn.database.base.Database

class FactRefRepositoryTest extends UnitTest with SharedTestObjects {

  test("routeFactRefs") {

    withDatabase { database =>

      setupRoute(database, 101, Subset.nlHiking, Seq(Fact.RouteIncomplete, Fact.RouteWithoutWays))
      setupRoute(database, 102, Subset.nlHiking, Seq(Fact.RouteIncomplete, Fact.RouteWithoutNodes))
      setupRoute(database, 103, Subset.beHiking, Seq(Fact.RouteIncomplete))

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.RouteIncomplete)
      subsetFactRefs should equal(
        SubsetFactRefs("relation", Seq(101, 102))
      )
    }
  }

  test("networkFactsWithElementIds NetworkExtraMemberNode") {

    withDatabase { database =>

      setupNetwork(database, 1, Subset.nlHiking, Fact.NetworkExtraMemberNode, "node", Seq(1001, 1002))
      setupNetwork(database, 2, Subset.nlHiking, Fact.NetworkExtraMemberNode, "node", Seq(1003))
      setupNetwork(database, 3, Subset.beHiking, Fact.NetworkExtraMemberNode, "node", Seq(1004, 1005))

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.NetworkExtraMemberNode)
      subsetFactRefs should equal(
        SubsetFactRefs("node", Seq(1001, 1002, 1003))
      )
    }
  }

  test("networkFactsWithElementIds NetworkExtraMemberWay") {

    withDatabase { database =>

      setupNetwork(database, 1, Subset.nlHiking, Fact.NetworkExtraMemberWay, "way", Seq(101, 102))
      setupNetwork(database, 2, Subset.nlHiking, Fact.NetworkExtraMemberWay, "way", Seq(103))
      setupNetwork(database, 3, Subset.beHiking, Fact.NetworkExtraMemberWay, "way", Seq(104, 105))

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.NetworkExtraMemberWay)
      subsetFactRefs should equal(
        SubsetFactRefs("way", Seq(101, 102, 103))
      )
    }
  }

  test("networkFactsWithElementIds NetworkExtraMemberRelation") {

    withDatabase { database =>

      setupNetwork(database, 1, Subset.nlHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(11, 12))
      setupNetwork(database, 2, Subset.nlHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(13))
      setupNetwork(database, 3, Subset.beHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(14, 15))

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.NetworkExtraMemberRelation)
      subsetFactRefs should equal(
        SubsetFactRefs("relation", Seq(11, 12, 13))
      )
    }
  }

  test("networkFactsWithRefs") {

    withDatabase { database =>

      setupNetworkNodeMemberMissing(database, 1, Subset.nlHiking, Seq(1001, 1002))
      setupNetworkNodeMemberMissing(database, 2, Subset.nlHiking, Seq(1003))
      setupNetworkNodeMemberMissing(database, 3, Subset.beHiking, Seq(1004, 1005))

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.NodeMemberMissing)
      subsetFactRefs should equal(
        SubsetFactRefs("node", Seq(1001, 1002, 1003))
      )
    }
  }

  test("IntegrityCheckFailed") {

    withDatabase { database =>

      setupNodeIntegrityCheckFailed(database, 1001, Subset.nlHiking)
      setupNodeIntegrityCheckFailed(database, 1002, Subset.nlHiking)
      setupNodeIntegrityCheckFailed(database, 1003, Subset.beHiking)

      val subsetFactRefs = repo(database).factRefs(Subset.nlHiking, Fact.IntegrityCheckFailed)
      subsetFactRefs should equal(
        SubsetFactRefs("node", Seq(1001, 1002))
      )
    }
  }

  private def repo(database: Database): FactRefRepository = {
    new FactRefRepositoryImpl(database)
  }

  private def setupRoute(database: Database, routeId: Long, subset: Subset, facts: Seq[Fact]): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(routeId),
        labels = Seq(
          Label.active,
          Label.facts,
          Label.networkType(subset.networkType),
          Label.location(subset.country.domain),
        ) ++ facts.map(Label.fact)
      )
    )
  }

  private def setupNodeIntegrityCheckFailed(database: Database, nodeId: Long, subset: Subset): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        labels = Seq(
          Label.active,
          Label.networkType(subset.networkType),
          Label.location(subset.country.domain),
          s"integrity-check-failed-${subset.networkType.name}"
        )
      )
    )
  }

  private def setupNetwork(
    database: Database,
    networkId: Long,
    subset: Subset,
    fact: Fact,
    elementType: String,
    elementIds: Seq[Long]
  ): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        networkId,
        country = Some(subset.country),
        summary = newNetworkSummary(
          networkType = subset.networkType,
        ),
        facts = Seq(
          NetworkFact(
            fact.name,
            Some(elementType),
            elementIds = Some(elementIds),
          )
        )
      )
    )
  }

  private def setupNetworkNodeMemberMissing(
    database: Database,
    networkId: Long,
    subset: Subset,
    nodeIds: Seq[Long]
  ): Unit = {
    database.networkInfos.save(
      newNetworkInfoDoc(
        networkId,
        country = Some(subset.country),
        summary = newNetworkSummary(
          networkType = subset.networkType,
        ),
        facts = Seq(
          NetworkFact(
            Fact.NodeMemberMissing.name,
            Some("node"),
            elements = Some(nodeIds.map(id => Ref(id, "")))
          )
        )
      )
    )
  }
}
