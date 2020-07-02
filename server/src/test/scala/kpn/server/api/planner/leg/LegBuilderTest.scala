package kpn.server.api.planner.leg

import kpn.api.common.LatLonImpl
import kpn.api.common.SharedTestObjects
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.Plan
import kpn.api.common.planner.PlanCoordinate
import kpn.api.common.planner.PlanFragment
import kpn.api.common.planner.PlanLeg
import kpn.api.common.planner.PlanNode
import kpn.api.common.planner.PlanRoute
import kpn.api.common.planner.PlanSegment
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.custom.NetworkType
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory

/*
        1m          2m          5m
  n1 ---r1--- n2 ---r2--- n3 ---r3--- n4
   \                     /
    ---------r4---------
             4m
 */
class LegBuilderTest extends UnitTest with MockFactory with SharedTestObjects {

  val data = new GraphTestData()

  test("node1 to node4") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(data.node1.id),
      sink = LegEnd.node(data.node4.id)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "1001-1004",
          source = LegEnd.node(data.node1.id),
          sink = LegEnd.node(data.node4.id),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10006", 4),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 2)),
            planRoute(planNode("10003", 2), planNode("10004", 3)),
            planRoute(planNode("10005", 3), planNode("10006", 4))
          )
        )
      )
    )
  }

  test("node1 to route3") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(data.node1.id),
      sink = LegEnd.route(data.legEndRoute3)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "1001-13.1",
          source = LegEnd.node(data.node1.id),
          sink = LegEnd.route(data.legEndRoute3),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10006", 4),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 2)),
            planRoute(planNode("10003", 2), planNode("10004", 3)),
            planRoute(planNode("10005", 3), planNode("10006", 4))
          )
        )
      )
    )
  }

  test("route1 to route3") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.route(data.legEndRoute1),
      sink = LegEnd.route(data.legEndRoute3)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "11.1-13.1",
          source = LegEnd.route(data.legEndRoute1),
          sink = LegEnd.route(data.legEndRoute3),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10006", 4),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 2)),
            planRoute(planNode("10003", 2), planNode("10004", 3)),
            planRoute(planNode("10005", 3), planNode("10006", 4))
          )
        )
      )
    )
  }

  test("route4 to route3") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.route(data.legEndRoute4),
      sink = LegEnd.route(data.legEndRoute3)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "14.1-13.1",
          source = LegEnd.route(data.legEndRoute4),
          sink = LegEnd.route(data.legEndRoute3),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10004", 4),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 3)),
            planRoute(planNode("10003", 3), planNode("10004", 4)),
          )
        )
      )
    )
  }

  test("node1 to route1") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(data.node1.id),
      sink = LegEnd.route(data.legEndRoute1)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "1001-11.1",
          source = LegEnd.node(data.node1.id),
          sink = LegEnd.route(data.legEndRoute1),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10002", 2),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 2)),
          )
        )
      )
    )
  }

  test("node1 to route4") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(data.node1.id),
      sink = LegEnd.route(data.legEndRoute4)
    )

    legBuilder().leg(params) should equal(
      Some(
        PlanLeg(
          featureId = "leg1",
          key = "1001-14.1",
          source = LegEnd.node(data.node1.id),
          sink = LegEnd.route(data.legEndRoute4),
          sourceNode = planNode("10001", 1),
          sinkNode = planNode("10002", 3),
          meters = 0,
          routes = Seq(
            planRoute(planNode("10001", 1), planNode("10002", 3)),
          )
        )
      )
    )
  }

  test("load node1 > node2") {

    legBuilder().plan(NetworkType.hiking, "1001-1002", encoded = false) should equal(
      Some(
        Plan(
          sourceNode = planNode("10002", 1),
          legs = Seq(
            PlanLeg(
              featureId = "10001",
              key = "1001-1002",
              source = LegEnd.node(data.node1.id),
              sink = LegEnd.node(data.node2.id),
              sourceNode = planNode("10002", 1),
              sinkNode = planNode("10003", 2),
              meters = 0,
              routes = Seq(
                planRoute(planNode("10002", 1), planNode("10003", 2)),
              )
            )
          )
        )
      )
    )
  }

  test("load node1 > route4 > node4") {

    //    legBuilder().load(NetworkType.hiking, "1001-14.1-1004", encoded = false) should equal(
    //      Some(
    //        Seq(
    //          RouteLeg(
    //            legId = "10001",
    //            routes = Seq(
    //              toRouteLegRoute(data.node1, data.node3)
    //            )
    //          ),
    //          RouteLeg(
    //            legId = "10002",
    //            routes = Seq(
    //              toRouteLegRoute(data.node3, data.node4)
    //            )
    //          )
    //        )
    //      )
    //    )
  }

  test("load empty plan") {
    legBuilder().plan(NetworkType.hiking, "", encoded = false) should equal(None)
  }

  test("load node1 > node3 > unknown-node") {
    legBuilder().plan(NetworkType.hiking, "1001-1003-9999", encoded = false) should equal(None)
  }

  test("load unknown-node > node1") {
    legBuilder().plan(NetworkType.hiking, "9999-1001", encoded = false) should equal(None)
  }

  private def legBuilder(): PlanBuilder = {
    new PlanBuilderImpl(data.graphRepository, data.routeRepository)
  }

  private def planRoute(sourcePlanNode: PlanNode, sinkPlanNode: PlanNode): PlanRoute = {

    PlanRoute(
      sourceNode = sourcePlanNode,
      sinkNode = sinkPlanNode,
      meters = 0,
      segments = Seq(
        PlanSegment(
          meters = 0,
          surface = "unpaved",
          colour = None,
          fragments = Seq(
            PlanFragment(
              meters = 0,
              orientation = 1,
              streetIndex = None,
              coordinate = sinkPlanNode.coordinate,
              sinkPlanNode.latLon
            )
          )
        )
      ),
      streets = Seq()
    )
  }

  private def toRouteLegNode(node: RouteNetworkNodeInfo): PlanNode = {
    PlanNode(
      "featureId",
      nodeId = node.id.toString,
      nodeName = node.name,
      coordinate = PlanCoordinate(0, 0),
      LatLonImpl(node.lat, node.lon)
    )
  }

  private def planNode(featureId: String, nodeNumber: Int): PlanNode = {
    val coordinate = PlanUtil.toCoordinate(nodeNumber.toDouble, nodeNumber.toDouble)
    PlanNode(
      featureId,
      s"${nodeNumber + 1000}",
      f"$nodeNumber%02d",
      coordinate,
      LatLonImpl(nodeNumber.toString, nodeNumber.toString)
    )
  }

}
