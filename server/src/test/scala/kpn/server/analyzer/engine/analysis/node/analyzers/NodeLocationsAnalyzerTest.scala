package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

class NodeLocationsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("node locations") {

    val analyzer = new NodeLocationsAnalyzerImpl(LocationAnalyzerTest.locationAnalyzer)

    val essen = NodeAnalysis(newRawNode(latitude = "51.46774", longitude = "4.46839"))
    analyzer.analyze(essen).locations should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerpen
        "be-2-11016" // Essen
      )
    )

    val baarleNassau = NodeAnalysis(newRawNode(latitude = "51.43948683099483", longitude = "4.931525588035583"))
    analyzer.analyze(baarleNassau).locations should equal(
      Seq(
        "nl",
        "nl-1-nb", // North Brabant
        "nl-2-744" // Baarle-Nassau
      )
    )

    val baarleHertog = NodeAnalysis(newRawNode(latitude = "51.43581846832453", longitude = "4.926767349243164"))
    analyzer.analyze(baarleHertog).locations should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerp province
        "be-2-13002" // Baarle-Hertog
      )
    )
  }
}
