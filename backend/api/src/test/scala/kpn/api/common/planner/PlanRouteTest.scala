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
        nodeLongName = None,
        coordinate = PlanCoordinate(1, 1),
        LatLonImpl("1", "1")
      ),
      sinkNode = PlanNode(
        featureId = "1004",
        nodeId = "1004",
        nodeName = "04",
        nodeLongName = None,
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
              coordinate = PlanCoordinate(2, 2),
              LatLonImpl("2", "2")
            ),
            PlanFragment(
              meters = 30,
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
              coordinate = PlanCoordinate(4, 4),
              LatLonImpl("4", "4")
            )
          )
        )
      )
    )

    planRoute.reverse should equal(
      PlanRoute(
        sourceNode = PlanNode(
          featureId = "1004",
          nodeId = "1004",
          nodeName = "04",
          None,
          coordinate = PlanCoordinate(4, 4),
          LatLonImpl("4", "4")
        ),
        sinkNode = PlanNode(
          featureId = "1001",
          nodeId = "1001",
          nodeName = "01",
          None,
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
                coordinate = PlanCoordinate(2, 2),
                LatLonImpl("2", "2")
              ),
              PlanFragment(
                meters = 10,
                coordinate = PlanCoordinate(1, 1),
                LatLonImpl("1", "1")
              )
            )
          )
        )
      )
    )
  }
}
