package kpn.server.analyzer.engine.changes.route.base

import kpn.core.util.UnitTest
import kpn.server.domain.StringCoordinate

class RouteGeometryWayAnalyzerTest extends UnitTest {

  test("empty result when no ways provided") {
    val geometryDiffWayIds = RouteGeometryWayAnalyzer.analyze(Seq.empty, Seq.empty)
    geometryDiffWayIds.isEmpty should equal(true)
  }

  test("find unchanged, removed, add and updated ways") {
    val unchangedWayId = 11
    val removedWayId = 12
    val addedWayId = 13
    val updatedWayId = 14

    val before = Seq(
      WayCoordinates(unchangedWayId, Seq.empty),
      WayCoordinates(removedWayId, Seq.empty),
      WayCoordinates(updatedWayId, Seq(StringCoordinate("1", "1"), StringCoordinate("2", "2"))),
    )
    val after = Seq(
      WayCoordinates(unchangedWayId, Seq.empty),
      WayCoordinates(addedWayId, Seq.empty),
      WayCoordinates(updatedWayId, Seq(StringCoordinate("1", "1"), StringCoordinate("3", "3"))),
    )

    val geometryDiffWayIds = RouteGeometryWayAnalyzer.analyze(before, after)

    geometryDiffWayIds should equal(
      GeometryDiffWayIds(
        removed = Set(removedWayId),
        added = Set(addedWayId),
        updated = Set(updatedWayId),
        unchanged = Set(unchangedWayId)
      )
    )
  }
}
