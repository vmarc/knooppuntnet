package kpn.server.analyzer.engine.tile

import kpn.api.custom.Tags
import kpn.core.TestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory

class TileChangeAnalyzerTest extends UnitTest with MockFactory with TestObjects {

  test("no tile task when no change with impact") {

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl()

    val routeAnalysis = CaseStudy.routeAnalysis("1029885")

    tileChangeAnalyzer.impactedTiles(routeAnalysis, routeAnalysis) should equal(Seq.empty)
  }

  test("tile tasks when change with impact") {

    pending

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl()

    val routeAnalysis = CaseStudy.routeAnalysis("1029885")

    val modifiedTags = routeAnalysis.route.tags ++ Tags.from("survey:date" -> "2019-11-08")
    val modifiedRoute = routeAnalysis.route.copy(tags = modifiedTags)
    val modifiedRouteAnalysis = routeAnalysis.copy(route = modifiedRoute)

    tileChangeAnalyzer.impactedTiles(routeAnalysis, modifiedRouteAnalysis) should equal(Seq.empty)
  }
}
