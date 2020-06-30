package kpn.server.api.planner.leg

import kpn.api.common.SharedTestObjects
import kpn.api.common.planner.LegBuildParams
import kpn.api.common.planner.LegEnd
import kpn.api.common.planner.RouteLeg
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
  val legBuilder = new LegBuilderImpl(data.graphRepository, data.routeRepository)

  test("node1 to node4") {

    val params = LegBuildParams(
      networkType = NetworkType.hiking.name,
      legId = "leg1",
      source = LegEnd.node(data.node1.id),
      sink = LegEnd.node(data.node4.id)
    )

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node2),
            PlanUtil.toRouteLegRoute(data.node2, data.node3),
            PlanUtil.toRouteLegRoute(data.node3, data.node4)
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

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node2),
            PlanUtil.toRouteLegRoute(data.node2, data.node3),
            PlanUtil.toRouteLegRoute(data.node3, data.node4)
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

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node2),
            PlanUtil.toRouteLegRoute(data.node2, data.node3),
            PlanUtil.toRouteLegRoute(data.node3, data.node4)
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

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node3),
            PlanUtil.toRouteLegRoute(data.node3, data.node4)
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

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node2)
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

    legBuilder.build(params) should equal(
      Some(
        RouteLeg(
          legId = "leg1",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node3)
          )
        )
      )
    )
  }

  test("load node1 > node2") {

    legBuilder.load(NetworkType.hiking, "1001-1002", encoded = false) should equal(
      Seq(
        RouteLeg(
          legId = "10001",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node2)
          )
        )
      )
    )
  }

  test("load node1 > route4 > node4") {

    legBuilder.load(NetworkType.hiking, "1001-14.1-1004", encoded = false) should equal(
      Seq(
        RouteLeg(
          legId = "10001",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node1, data.node3)
          )
        ),
        RouteLeg(
          legId = "10002",
          routes = Seq(
            PlanUtil.toRouteLegRoute(data.node3, data.node4)
          )
        )
      )
    )
  }

  test("load empty plan") {
    legBuilder.load(NetworkType.hiking, "", encoded = false) should equal(Seq())
  }

  test("load node1 > node3 > unknown-node") {
    legBuilder.load(NetworkType.hiking, "1001-1003-9999", encoded = false) should equal(Seq())
  }

  test("load unknown-node > node1") {
    legBuilder.load(NetworkType.hiking, "9999-1001", encoded = false) should equal(Seq())
  }

}
