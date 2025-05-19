package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.data.raw.RawNode
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest

class BaseNodeLocationAnalyzerTest extends UnitTest with SharedTestObjects {

  test("node locations - Essen") {
    val essen = newRawNode(latitude = "51.46774", longitude = "4.46839")
    analyze(essen) should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerpen
        "be-2-11016" // Essen
      )
    )
  }

  test("node locations - Baarle Nassau") {
    val baarleNassau = newRawNode(latitude = "51.43948683099483", longitude = "4.931525588035583")
    analyze(baarleNassau) should equal(
      Seq(
        "nl",
        "nl-1-nb", // North Brabant
        "nl-2-744" // Baarle-Nassau
      )
    )
  }

  test("node locations - Baarle Hertog") {
    val baarleHertog = newRawNode(latitude = "51.43581846832453", longitude = "4.926767349243164")
    analyze(baarleHertog) should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerp province
        "be-2-13002" // Baarle-Hertog
      )
    )
  }

  private def analyze(rawNode: RawNode): Seq[String] = {
    val context = BaseNodeAnalysisContext(rawNode)
    val analyzer = new BaseNodeLocationAnalyzer(LocationAnalyzerTest.locationAnalyzer)
    val updatedContext = analyzer.analyze(context)
    updatedContext.locations
  }
}
