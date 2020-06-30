package kpn.api.common.planner

import kpn.core.util.UnitTest

class RouteLegRouteTest extends UnitTest {

  test("reverse") {
    val routeLegRoute = RouteLegRoute(
      source = RouteLegNode(
        nodeId = "1001",
        nodeName = "01",
        lat = "1",
        lon = "1"
      ),
      sink = RouteLegNode(
        nodeId = "1004",
        nodeName = "04",
        lat = "4",
        lon = "4"
      ),
      meters = 100,
      segments = Seq(
        RouteLegSegment(
          meters = 40,
          surface = "paved",
          colour = Some("colour1"),
          fragments = Seq(
            RouteLegFragment(
              lat = "2",
              lon = "2",
              meters = 10,
              orientation = 1,
              streetIndex = Some(0)
            ),
            RouteLegFragment(
              lat = "3",
              lon = "3",
              meters = 30,
              orientation = 1,
              streetIndex = Some(0)
            )
          )
        ),
        RouteLegSegment(
          meters = 50,
          surface = "unpaved",
          colour = Some("colour2"),
          fragments = Seq(
            RouteLegFragment(
              lat = "4",
              lon = "4",
              meters = 50,
              orientation = 1,
              streetIndex = Some(1)
            )
          )
        )
      ),
      streets = Seq(
        "street1",
        "street2"
      )
    )

    routeLegRoute.reverse should equal(
      RouteLegRoute(
        source = RouteLegNode(
          nodeId = "1004",
          nodeName = "04",
          lat = "4",
          lon = "4"
        ),
        sink = RouteLegNode(
          nodeId = "1001",
          nodeName = "01",
          lat = "1",
          lon = "1"
        ),
        meters = 100,
        segments = Seq(
          RouteLegSegment(
            meters = 50,
            surface = "unpaved",
            colour = Some("colour2"),
            fragments = Seq(
              RouteLegFragment(
                lat = "3",
                lon = "3",
                meters = 50,
                orientation = 1,
                streetIndex = Some(1)
              )
            )
          ),
          RouteLegSegment(
            meters = 40,
            surface = "paved",
            colour = Some("colour1"),
            fragments = Seq(
              RouteLegFragment(
                lat = "2",
                lon = "2",
                meters = 30,
                orientation = 1,
                streetIndex = Some(0)
              ),
              RouteLegFragment(
                lat = "1",
                lon = "1",
                meters = 10,
                orientation = 1,
                streetIndex = Some(0)
              )
            )
          )
        ),
        streets = Seq(
          "street1",
          "street2"
        )
      )
    )
  }
}
