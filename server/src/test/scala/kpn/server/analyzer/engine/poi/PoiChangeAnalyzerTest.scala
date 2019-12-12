package kpn.server.analyzer.engine.poi

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.country.CountryAnalyzer
import kpn.server.analyzer.engine.changes.changes.OsmChange
import kpn.server.analyzer.engine.tile.TileCalculator
import kpn.server.repository.PoiRepository
import kpn.server.repository.TaskRepository
import org.scalamock.scalatest.MockFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class PoiChangeAnalyzerTest extends FunSuite with Matchers with SharedTestObjects with MockFactory {

  test("node poi add") {
    val poiRepository = stub[PoiRepository]

    val knownPoiCache = stub[KnownPoiCache]
    val countryAnalyzer = stub[CountryAnalyzer]
    val tileCalculator = stub[TileCalculator]
    val taskRepository = stub[TaskRepository]
    val poiUpdateProcessor = stub[PoiUpdateProcessor]
    val poiDeleteProcessor = stub[PoiDeleteProcessor]

    val poiChangeAnalyzer = new PoiChangeAnalyzerImpl(
      knownPoiCache,
      poiRepository,
      countryAnalyzer,
      tileCalculator,
      taskRepository,
      poiUpdateProcessor,
      poiDeleteProcessor
    )

    val essenNode = newRawNode(
      id = 123L,
      latitude = "51.46774",
      longitude = "4.46839",
      tags = Tags.from("shop" -> "bicycle")
    )

    val osmChange = OsmChange(Seq(Change.create(essenNode)))

    poiChangeAnalyzer.analyze(osmChange)


    // assert poiRepository.add called with poi
    // assert poiChangeAnalyzer.knownPois contains poi
  }

  test("known node poi update") {


  }

  test("unknown node poi update") {


  }

  test("known node poi delete") {


  }

  test("unknown node poi delete") {


  }

}
