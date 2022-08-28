package kpn.server.analyzer.engine.tile

import kpn.api.custom.Day
import kpn.api.custom.NetworkType
import kpn.core.TestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.tiles.domain.RouteTileAnalysis
import kpn.server.analyzer.engine.tiles.domain.RouteTileSegment
import kpn.server.analyzer.engine.tiles.domain.ZoomLevelRouteTileSegments
import org.scalamock.scalatest.MockFactory

class RouteTileChangeAnalyzerTest extends UnitTest with MockFactory with TestObjects {

  test("no tiles when no change with impact") {
    val before = buildRouteAnalysis()
    val after = before.copy()
    impactedTiles(before, after) should equal(Seq.empty)
  }

  test("impact when networkType changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      route = before.route.copy(
        summary = before.route.summary.copy(
          networkType = NetworkType.cycling
        ),
        tiles = Seq("cycling-tile-1")
      )
    )
    impactedTiles(before, after) should equal(Seq("cycling-tile-1", "hiking-tile-1"))
  }

  test("impact when routeName changes") {
    val before = buildRouteAnalysis()
    val after = before.copy(
      route = before.route.copy(
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
      route = before.route.copy(
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
      route = before.route.copy(
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
      route = before.route.copy(
        tiles = Seq("hiking-tile-2")
      ),
      tileAnalysis = before.tileAnalysis.copy(
        state = Some("proposed")
      )
    )
    impactedTiles(before, after) should equal(Seq("hiking-tile-1", "hiking-tile-2"))
  }

  private def buildRouteAnalysis(): RouteAnalysis = {
    RouteAnalysis(
      relation = null,
      route = newRouteDoc(
        newRouteSummary(
          id = 10,
          networkType = NetworkType.hiking
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
              RouteTileSegment(
                pathId = 101,
                oneWay = false,
                surface = "",
                lines = Seq.empty
              )
            )
          )
        )
      )
    )
  }

  private def impactedTiles(before: RouteAnalysis, after: RouteAnalysis): Seq[String] = {
    val tileChangeAnalyzer = new RouteTileChangeAnalyzerImpl()
    tileChangeAnalyzer.impactedTiles(before, after)
  }
}
