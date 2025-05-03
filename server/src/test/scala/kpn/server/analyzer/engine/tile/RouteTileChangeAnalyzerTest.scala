package kpn.server.analyzer.engine.tile

import kpn.api.common.RouteType
import kpn.api.custom.Day
import kpn.core.TestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.scalamock.scalatest.MockFactory

class RouteTileChangeAnalyzerTest extends UnitTest with MockFactory with TestObjects {

  test("no tiles when no change with impact") {
    val before = buildRouteAnalysis()
    val after = before.copy()
    impactedTiles(before, after) should equal(Seq.empty)
  }

  test("impact when routeType changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      baseRoute = before.baseRoute.copy(
        summary = before.baseRoute.summary.copy(
          routeTypes = Seq(RouteType.cycling)
        ),
        tiles = Seq("cycling-tile-1")
      )
    )
    impactedTiles(before, after) should equal(Seq("cycling-tile-1", "hiking-tile-1"))
  }

  test("impact when routeName changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      baseRoute = before.baseRoute.copy(
        tiles = Seq("hiking-tile-2")
      ),
      tileAnalysis = before.tileAnalysis.copy(
        routeName = "02-03"
      )
    )
    impactedTiles(before, after) should equal(Seq("hiking-tile-1", "hiking-tile-2"))
  }

  test("impact when layer changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      baseRoute = before.baseRoute.copy(
        tiles = Seq("hiking-tile-2")
      ),
      tileAnalysis = before.tileAnalysis.copy(
        layer = "route-error"
      )
    )
    impactedTiles(before, after) should equal(Seq("hiking-tile-1", "hiking-tile-2"))
  }

  test("impact when surveyDate changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      baseRoute = before.baseRoute.copy(
        tiles = Seq("hiking-tile-2")
      ),
      tileAnalysis = before.tileAnalysis.copy(
        surveyDate = Some(Day(2020, 8, 11))
      )
    )
    impactedTiles(before, after) should equal(Seq("hiking-tile-1", "hiking-tile-2"))
  }

  test("impact when state changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      baseRoute = before.baseRoute.copy(
        tiles = Seq("hiking-tile-2")
      ),
      tileAnalysis = before.tileAnalysis.copy(
        state = Some("proposed")
      )
    )
    impactedTiles(before, after) should equal(Seq("hiking-tile-1", "hiking-tile-2"))
  }

  private def buildRouteAnalysis(): RouteDetailAnalysis = {
    RouteDetailAnalysis(
      relation = null,
      baseRoute = newBaseRouteDoc(
        newRouteSummary(
          id = 10,
          routeTypes = Seq(RouteType.hiking)
        ),
        tiles = Seq("hiking-tile-1")
      ),
      tileAnalysis = RouteTileAnalysis(
        routeName = "01-02",
        layer = "route",
        surveyDate = None,
        state = None,
        zoomLevelSegments = Seq(
          ZoomLevelRouteTileSegments(
            zoomLevel = 10,
            segments = Seq(
              TileDataRouteSegment(
                0,
                0,
                pathIds = Seq(101),
                oneWay = false,
                surface = "",
                worldCoordinates = Seq.empty
              )
            )
          )
        )
      )
    )
  }

  private def impactedTiles(before: RouteDetailAnalysis, after: RouteDetailAnalysis): Seq[String] = {
    val tileChangeAnalyzer = new RouteTileChangeAnalyzerImpl()
    // TODO redesign - tileChangeAnalyzer.impactedTiles(before, after)
    Seq.empty
  }
}
