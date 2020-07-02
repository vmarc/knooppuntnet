package kpn.api.common.planner

import kpn.api.common.LatLonImpl
import kpn.core.util.UnitTest

class PlanRouteTest extends UnitTest {

  test("reverse") {
    val planRoute = PlanRoute(
      sourceNode = PlanNode(
        featureId = "1001",
        nodeId = "1001",
        nodeName = "01",
        coordinate = PlanCoordinate(1, 1),
        LatLonImpl("1", "1")
      ),
      sinkNode = PlanNode(
        featureId = "1004",
        nodeId = "1004",
        nodeName = "04",
        coordinate = PlanCoordinate(4, 4),
        LatLonImpl("4", "4")
      ),
      meters = 100,
      segments = Seq(
        PlanSegment(
          meters = 40,
          surface = "paved",
          colour = Some("colour1"),
          fragments = Seq(
            PlanFragment(
              meters = 10,
              orientation = 1,
              streetIndex = Some(0),
              coordinate = PlanCoordinate(2, 2),
              LatLonImpl("2", "2")
            ),
            PlanFragment(
              meters = 30,
              orientation = 1,
              streetIndex = Some(0),
              coordinate = PlanCoordinate(3, 3),
              LatLonImpl("3", "3")
            )
          )
        ),
        PlanSegment(
          meters = 50,
          surface = "unpaved",
          colour = Some("colour2"),
          fragments = Seq(
            PlanFragment(
              meters = 50,
              orientation = 1,
              streetIndex = Some(1),
              coordinate = PlanCoordinate(4, 4),
              LatLonImpl("4", "4")
            )
          )
        )
      ),
      streets = Seq(
        "street1",
        "street2"
      )
    )

    planRoute.reverse should equal(
      PlanRoute(
        sourceNode = PlanNode(
          featureId = "1004",
          nodeId = "1004",
          nodeName = "04",
          coordinate = PlanCoordinate(4, 4),
          LatLonImpl("4", "4")
        ),
        sinkNode = PlanNode(
          featureId = "1001",
          nodeId = "1001",
          nodeName = "01",
          coordinate = PlanCoordinate(1, 1),
          LatLonImpl("1", "1")
        ),
        meters = 100,
        segments = Seq(
          PlanSegment(
            meters = 50,
            surface = "unpaved",
            colour = Some("colour2"),
            fragments = Seq(
              PlanFragment(
                meters = 50,
                orientation = 1,
                streetIndex = Some(1),
                coordinate = PlanCoordinate(3, 3),
                LatLonImpl("3", "3")
              )
            )
          ),
          PlanSegment(
            meters = 40,
            surface = "paved",
            colour = Some("colour1"),
            fragments = Seq(
              PlanFragment(
                meters = 30,
                orientation = 1,
                streetIndex = Some(0),
                coordinate = PlanCoordinate(2, 2),
                LatLonImpl("2", "2")
              ),
              PlanFragment(
                meters = 10,
                orientation = 1,
                streetIndex = Some(0),
                coordinate = PlanCoordinate(1, 1),
                LatLonImpl("1", "1")
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
