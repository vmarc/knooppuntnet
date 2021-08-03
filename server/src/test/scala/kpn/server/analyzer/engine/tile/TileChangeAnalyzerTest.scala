package kpn.server.analyzer.engine.tile

import kpn.api.custom.Tags
import kpn.core.TestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory

class TileChangeAnalyzerTest extends UnitTest with MockFactory with TestObjects {

  test("analyzeRoute") {

    pending // old tagging is not supported anymore
    val taskRepository = stub[TaskRepository]
    val routeTileCalculator = new RouteTileCalculatorImpl(new TileCalculatorImpl())

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl(taskRepository, routeTileCalculator)

    val routeAnalysis = CaseStudy.routeAnalysis("1029885", oldTagging = true)

    tileChangeAnalyzer.analyzeRoute(routeAnalysis)

    (taskRepository.add _).verify("tile-task:cycling-6-33-21") // OK
    (taskRepository.add _).verify("tile-task:cycling-7-66-42") // OK
    (taskRepository.add _).verify("tile-task:cycling-8-132-85") // OK
    (taskRepository.add _).verify("tile-task:cycling-9-264-170") // OK
    (taskRepository.add _).verify("tile-task:cycling-10-528-340") // OK
    (taskRepository.add _).verify("tile-task:cycling-11-1057-681") // OK

    (taskRepository.add _).verify("tile-task:cycling-12-2115-1362") // OK
    (taskRepository.add _).verify("tile-task:cycling-12-2115-1363") // OK

    (taskRepository.add _).verify("tile-task:cycling-13-4231-2725") // OK
    (taskRepository.add _).verify("tile-task:cycling-13-4231-2726") // OK

    (taskRepository.add _).verify("tile-task:cycling-14-8462-5451") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8462-5452") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5451") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5452") // OK
  }

  test("no tile task when no change with impact") {

    val taskRepository = stub[TaskRepository]
    val routeTileCalculator = new RouteTileCalculatorImpl(new TileCalculatorImpl())

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl(taskRepository, routeTileCalculator)

    val routeAnalysis = CaseStudy.routeAnalysis("1029885")

    tileChangeAnalyzer.analyzeRouteChange(routeAnalysis, routeAnalysis)

    (taskRepository.add _).verify(*).never()
  }

  test("tile tasks when change with impact") {

    pending // old tagging is not supported anymore

    val taskRepository = stub[TaskRepository]
    val routeTileCalculator = new RouteTileCalculatorImpl(new TileCalculatorImpl())

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl(taskRepository, routeTileCalculator)

    val routeAnalysis = CaseStudy.routeAnalysis("1029885", oldTagging = true)

    val modifiedTags = routeAnalysis.route.tags ++ Tags.from("survey:date" -> "2019-11-08")
    val modifiedRoute = routeAnalysis.route.copy(tags = modifiedTags)
    val modifiedRouteAnalysis = routeAnalysis.copy(route = modifiedRoute)

    tileChangeAnalyzer.analyzeRouteChange(routeAnalysis, modifiedRouteAnalysis)

    (taskRepository.add _).verify(*).repeated(14)
  }
}
