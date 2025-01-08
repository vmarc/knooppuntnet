package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.RouteType
import kpn.api.common.RouteType.cycling
import kpn.api.common.RouteType.hiking
import kpn.api.common.RouteType.horseRiding
import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Way
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class SurfaceAnalyzerTest extends UnitTest with SharedTestObjects {

  test("prioritize 'footway:surface' for hiking and 'cycleway:surface' for cycling") {

    val way1 = wayWithTags(
      "footway:surface" -> "grass",
      "cycleway:surface" -> "concrete",
      "surface" -> "asphalt",
    )

    surface(hiking, way1) should equal("unpaved")
    surface(cycling, way1) should equal("paved")
    surface(horseRiding, way1) should equal("paved")

    val way2 = wayWithTags(
      "footway:surface" -> "paving_stones",
      "cycleway:surface" -> "grass",
      "surface" -> "asphalt",
    )

    surface(hiking, way2) should equal("paved")
    surface(cycling, way2) should equal("unpaved")
    surface(horseRiding, way2) should equal("paved")

    val way3 = wayWithTags(
      "surface" -> "grass"
    )

    surface(hiking, way3) should equal("unpaved")
    surface(cycling, way3) should equal("unpaved")
    surface(horseRiding, way3) should equal("unpaved")
  }

  private def surface(routeType: RouteType, way: Way): String = {
    new SurfaceAnalyzer(Seq(routeType), way).surface()
  }

  private def wayWithTags(tags: (String, String)*): Way = {
    newWay(
      101L,
      tags = Tags.from(tags.toMap)
    )
  }
}
