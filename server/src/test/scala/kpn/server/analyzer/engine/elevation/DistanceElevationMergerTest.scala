package kpn.server.analyzer.engine.elevation

import kpn.core.util.UnitTest

class DistanceElevationMergerTest extends UnitTest {

  test("empty - nothing to merge") {
    DistanceElevationMerger.merge(Seq.empty) shouldBe empty
  }

  test("single distance/elevation - nothing to merge") {
    val des = Seq(
      DistanceElevation(10, 10)
    )
    DistanceElevationMerger.merge(des).shouldMatchTo(
      Seq(
        DistanceElevation(10, 10)
      )
    )
  }

  test("merge identical elevations") {
    val des = Seq(
      DistanceElevation(10, 10),
      DistanceElevation(20, 10)
    )
    DistanceElevationMerger.merge(des).shouldMatchTo(
      Seq(
        DistanceElevation(30, 10)
      )
    )
  }

  test("merge multiple identical elevations") {
    val des = Seq(
      DistanceElevation(10, 10),
      DistanceElevation(20, 10),
      DistanceElevation(30, 10),
      DistanceElevation(40, 20),
      DistanceElevation(50, 20),
      DistanceElevation(60, 30)
    )
    DistanceElevationMerger.merge(des).shouldMatchTo(
      Seq(
        DistanceElevation(60, 10),
        DistanceElevation(90, 20),
        DistanceElevation(60, 30)
      )
    )
  }
}
