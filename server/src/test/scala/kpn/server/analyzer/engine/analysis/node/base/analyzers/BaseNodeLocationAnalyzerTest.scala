package kpn.server.analyzer.engine.analysis.node.base.analyzers

import kpn.api.common.LatLon
import kpn.api.common.data.raw.RawNode
import kpn.core.test.Locations
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.location.LocationAnalyzerTest

class BaseNodeLocationAnalyzerTest extends UnitTest with SharedTestObjects {

  test("node locations - Essen") {
    val essen = buildNode(Locations.essen)
    analyze(essen) should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerpen
        "be-2-11016" // Essen
      )
    )
  }

  test("node locations - Baarle Nassau") {
    val baarleNassau = buildNode(Locations.baarleNassau)
    analyze(baarleNassau) should equal(
      Seq(
        "nl",
        "nl-1-nb", // North Brabant
        "nl-2-744" // Baarle-Nassau
      )
    )
  }

  test("node locations - Baarle Hertog") {
    val baarleHertog = buildNode(Locations.baarleHertog)
    analyze(baarleHertog) should equal(
      Seq(
        "be",
        "be-1-10000", // Antwerp province
        "be-2-13002" // Baarle-Hertog
      )
    )
  }

  private def buildNode(latLon: LatLon): RawNode = {
    newRawNode(latitude = latLon.latitude, longitude = latLon.longitude)
  }

  private def analyze(rawNode: RawNode): Seq[String] = {
    val context = BaseNodeAnalysisContext(rawNode)
    val analyzer = new BaseNodeLocationAnalyzer(LocationAnalyzerTest.locationAnalyzer)
    val updatedContext = analyzer.analyze(context)
    updatedContext.locations
  }
}
