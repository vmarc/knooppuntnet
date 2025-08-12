package kpn.server.repository

import kpn.api.common.Fact
import kpn.api.common.NetworkFact
import kpn.api.common.common.Ref
import kpn.api.common.subset.SubsetFactRefs
import kpn.api.custom.Subset
import kpn.core.doc.Label
import kpn.core.test.MongoTest
import kpn.core.test.TestObjects.newNetworkDoc
import kpn.core.test.TestObjects.newNetworkSummary
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary

class FactRefRepositoryTest extends MongoTest {

  test("routeFactRefs") {

    setupRoute(101, Subset.nlHiking, Seq(Fact.RouteIncomplete, Fact.RouteWithoutWays))
    setupRoute(102, Subset.nlHiking, Seq(Fact.RouteIncomplete, Fact.RouteWithoutNodes))
    setupRoute(103, Subset.beHiking, Seq(Fact.RouteIncomplete))

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.RouteIncomplete)
    subsetFactRefs should equal(
      SubsetFactRefs("relation", Seq(101, 102))
    )
  }

  test("networkFactsWithElementIds NetworkExtraMemberNode") {

    setupNetwork(1, Subset.nlHiking, Fact.NetworkExtraMemberNode, "node", Seq(1001, 1002))
    setupNetwork(2, Subset.nlHiking, Fact.NetworkExtraMemberNode, "node", Seq(1003))
    setupNetwork(3, Subset.beHiking, Fact.NetworkExtraMemberNode, "node", Seq(1004, 1005))

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.NetworkExtraMemberNode)
    subsetFactRefs should equal(
      SubsetFactRefs("node", Seq(1001, 1002, 1003))
    )
  }

  test("networkFactsWithElementIds NetworkExtraMemberWay") {

    setupNetwork(1, Subset.nlHiking, Fact.NetworkExtraMemberWay, "way", Seq(101, 102))
    setupNetwork(2, Subset.nlHiking, Fact.NetworkExtraMemberWay, "way", Seq(103))
    setupNetwork(3, Subset.beHiking, Fact.NetworkExtraMemberWay, "way", Seq(104, 105))

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.NetworkExtraMemberWay)
    subsetFactRefs should equal(
      SubsetFactRefs("way", Seq(101, 102, 103))
    )
  }

  test("networkFactsWithElementIds NetworkExtraMemberRelation") {

    setupNetwork(1, Subset.nlHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(11, 12))
    setupNetwork(2, Subset.nlHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(13))
    setupNetwork(3, Subset.beHiking, Fact.NetworkExtraMemberRelation, "relation", Seq(14, 15))

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.NetworkExtraMemberRelation)
    subsetFactRefs should equal(
      SubsetFactRefs("relation", Seq(11, 12, 13))
    )
  }

  test("networkFactsWithRefs") {

    setupNetworkNodeMemberMissing(1, Subset.nlHiking, Seq(1001, 1002))
    setupNetworkNodeMemberMissing(2, Subset.nlHiking, Seq(1003))
    setupNetworkNodeMemberMissing(3, Subset.beHiking, Seq(1004, 1005))

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.NodeMemberMissing)
    subsetFactRefs should equal(
      SubsetFactRefs("node", Seq(1001, 1002, 1003))
    )
  }

  test("IntegrityCheckFailed") {

    setupNodeIntegrityCheckFailed(1001, Subset.nlHiking)
    setupNodeIntegrityCheckFailed(1002, Subset.nlHiking)
    setupNodeIntegrityCheckFailed(1003, Subset.beHiking)

    val subsetFactRefs = repo().factRefs(Subset.nlHiking, Fact.IntegrityCheckFailed)
    subsetFactRefs should equal(
      SubsetFactRefs("node", Seq(1001, 1002))
    )
  }

  private def repo(): FactRefRepository = {
    new FactRefRepositoryImpl(database)
  }

  private def setupRoute(routeId: Long, subset: Subset, facts: Seq[Fact]): Unit = {
    database.routes.save(
      newRouteDoc(
        newRouteSummary(routeId),
        labels = Seq(
          Label.facts,
          Label.routeType(subset.routeType),
          Label.location(subset.country.entryName),
        ) ++ facts.map(Label.fact)
      )
    )
  }

  private def setupNodeIntegrityCheckFailed(nodeId: Long, subset: Subset): Unit = {
    database.nodes.save(
      newNodeDoc(
        nodeId,
        labels = Seq(
          Label.routeType(subset.routeType),
          Label.location(subset.country.entryName),
          s"integrity-check-failed-${subset.routeType.entryName}"
        )
      )
    )
  }

  private def setupNetwork(
    networkId: Long,
    subset: Subset,
    fact: Fact,
    elementType: String,
    elementIds: Seq[Long]
  ): Unit = {
    database.networks.save(
      newNetworkDoc(
        networkId,
        country = Some(subset.country),
        summary = newNetworkSummary(
          routeType = subset.routeType,
        ),
        facts = Seq(
          NetworkFact(
            fact,
            Some(elementType),
            elementIds = Some(elementIds),
          )
        )
      )
    )
  }

  private def setupNetworkNodeMemberMissing(
    networkId: Long,
    subset: Subset,
    nodeIds: Seq[Long]
  ): Unit = {
    database.networks.save(
      newNetworkDoc(
        networkId,
        country = Some(subset.country),
        summary = newNetworkSummary(
          routeType = subset.routeType,
        ),
        facts = Seq(
          NetworkFact(
            Fact.NodeMemberMissing,
            Some("node"),
            elements = Some(nodeIds.map(id => Ref(id, "")))
          )
        )
      )
    )
  }
}
