package kpn.server.analyzer.engine.tile

import kpn.core.TestObjects
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TileChangeAnalyzerTest extends FunSuite with Matchers with MockFactory with TestObjects {

  test("analyzeRoute") {

    val taskRepository = stub[TaskRepository]
    val routeTileAnalyzer = new RouteTileAnalyzerImpl(new TileCalculatorImpl())

    val tileChangeAnalyzer: TileChangeAnalyzer = new TileChangeAnalyzerImpl(taskRepository, routeTileAnalyzer)

    val routeAnalysis = CaseStudy.routeAnalysis("1029885")

    tileChangeAnalyzer.analyzeRoute(routeAnalysis)

    (taskRepository.add _).verify("tile-task:cycling-6-33-21") // OK
    (taskRepository.add _).verify("tile-task:cycling-7-66-42") // OK
    (taskRepository.add _).verify("tile-task:cycling-8-132-85") // OK
    (taskRepository.add _).verify("tile-task:cycling-9-264-170") // OK
    (taskRepository.add _).verify("tile-task:cycling-10-528-340") // OK
    (taskRepository.add _).verify("tile-task:cycling-11-1057-681") // OK

    (taskRepository.add _).verify("tile-task:cycling-12-2115-1361") // not needed ?
    (taskRepository.add _).verify("tile-task:cycling-12-2115-1362") // OK
    (taskRepository.add _).verify("tile-task:cycling-12-2115-1363") // OK
    (taskRepository.add _).verify("tile-task:cycling-12-2115-1364") // not needed ?

    (taskRepository.add _).verify("tile-task:cycling-13-4231-2724") // not needed ?
    (taskRepository.add _).verify("tile-task:cycling-13-4231-2725") // OK
    (taskRepository.add _).verify("tile-task:cycling-13-4231-2726") // OK
    (taskRepository.add _).verify("tile-task:cycling-13-4231-2727") // not needed ?

    (taskRepository.add _).verify("tile-task:cycling-14-8462-5450") // not needed ?
    (taskRepository.add _).verify("tile-task:cycling-14-8462-5451") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8462-5452") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8462-5453") // not needed ?
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5450") // not needed ?
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5451") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5452") // OK
    (taskRepository.add _).verify("tile-task:cycling-14-8463-5453") // not needed ?
  }

}
