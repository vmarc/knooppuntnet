package kpn.core.planner

import kpn.core.db.couch.Couch
import kpn.core.repository.NodeRepository
import kpn.core.repository.RouteRepository
import kpn.shared.NetworkType
import kpn.shared.SharedTestObjects
import kpn.shared.data.Tags
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PlanBuilderTest extends FunSuite with Matchers with MockFactory with SharedTestObjects {

  test("nodes, intermediate nodes and routes") {

    val nodeInfo1 = newNodeInfo(id = 1, tags = Tags.from("rwn_ref" -> "01"))
    val nodeInfo3 = newNodeInfo(id = 3, tags = Tags.from("rwn_ref" -> "03"))
    val nodeInfo5 = newNodeInfo(id = 5, tags = Tags.from("rwn_ref" -> "05"))
    val nodeInfos = Seq(nodeInfo1, nodeInfo3, nodeInfo5)

    val route2 = newRoute(id = 2, name = "01-03", meters = 100)
    val route4 = newRoute(id = 4, name = "03-05", meters = 200)
    val routes = Seq(route2, route4)

    val nodeRepository = mock[NodeRepository]
    (nodeRepository.nodesWithIds _).expects(Seq[Long](1, 3, 5), Couch.uiTimeout, false).returning(nodeInfos)

    val routeRepository = mock[RouteRepository]
    (routeRepository.routesWithIds _).expects(Seq[Long](2, 4), Couch.uiTimeout).returning(routes)

    val encodedPlan = EncodedPlan("N1-R2-I3-R4-N5")
    val plan = new PlanBuilder(nodeRepository, routeRepository).build(NetworkType.hiking, encodedPlan)

    plan.items should equal(
      Seq(
        PlanNode(1, "01", 0),
        PlanRoute(2, "01-03", 100),
        PlanIntermediateNode(3, "03", 100),
        PlanRoute(4, "03-05", 300),
        PlanNode(5, "05", 300)
      )
    )
  }

  test("node not found in database") {
    val nodeInfo1 = newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01"))
    val r = newRoute(2, name = "01-03", meters = 100)

    val nodeRepository = mock[NodeRepository]
    (nodeRepository.nodesWithIds _).expects(Seq[Long](1, 3), Couch.uiTimeout, false).returning(Seq(nodeInfo1))

    val routeRepository = mock[RouteRepository]
    (routeRepository.routesWithIds _).expects(Seq[Long](2), Couch.uiTimeout).returning(Seq(r))

    val encodedPlan = EncodedPlan("N1-R2-N3") // N3 not in repository
    val plan = new PlanBuilder(nodeRepository, routeRepository).build(NetworkType.hiking, encodedPlan)

    plan.items should equal(
      Seq(
        PlanNode(1, "01", 0),
        PlanRoute(2, "01-03", 100),
        PlanMessageItem(NodeNotFoundInDatabase(3))
      )
    )
  }

  test("intermediate node not found in database") {
    val nodeInfo1 = newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01"))
    val r = newRoute(2, name = "01-03", meters = 100)

    val nodeRepository = mock[NodeRepository]
    (nodeRepository.nodesWithIds _).expects(Seq[Long](1, 3), Couch.uiTimeout, false).returning(Seq(nodeInfo1))

    val routeRepository = mock[RouteRepository]
    (routeRepository.routesWithIds _).expects(Seq[Long](2), Couch.uiTimeout).returning(Seq(r))

    val encodedPlan = EncodedPlan("N1-R2-I3") // N3 not in repository
    val plan = new PlanBuilder(nodeRepository, routeRepository).build(NetworkType.hiking, encodedPlan)

    plan.items should equal(
      Seq(
        PlanNode(1, "01", 0),
        PlanRoute(2, "01-03", 100),
        PlanMessageItem(NodeNotFoundInDatabase(3))
      )
    )
  }

  test("route not found in database") {
    val nodeInfo1 = newNodeInfo(1, tags = Tags.from("rwn_ref" -> "01"))
    val nodeInfo3 = newNodeInfo(3, tags = Tags.from("rwn_ref" -> "03"))

    val nodeRepository = mock[NodeRepository]
    (nodeRepository.nodesWithIds _).expects(Seq[Long](1, 3), Couch.uiTimeout, false).returning(Seq(nodeInfo1, nodeInfo3))

    val routeRepository = mock[RouteRepository]
    (routeRepository.routesWithIds _).expects(Seq[Long](2), Couch.uiTimeout).returning(Seq())

    val encodedPlan = EncodedPlan("N1-R2-N3") // N3 not in repository
    val plan = new PlanBuilder(nodeRepository, routeRepository).build(NetworkType.hiking, encodedPlan)

    plan.items should equal(
      Seq(
        PlanNode(1, "01", 0),
        PlanMessageItem(RouteNotFoundInDatabase(2)),
        PlanNode(3, "03", 0)
      )
    )
  }
}
