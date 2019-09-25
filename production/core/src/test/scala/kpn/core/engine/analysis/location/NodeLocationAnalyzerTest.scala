package kpn.core.engine.analysis.location

import kpn.shared.Location
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeLocationAnalyzerTest extends FunSuite with Matchers {

  test("NodeLocationAnalyzer") {

    val locationDefinitions = new LocationsReader().read()
    val analyzer = new NodeLocationAnalyzerImpl(locationDefinitions)

    // Essen
    analyzer.locate("51.46774", "4.46839") should equal(Some(Location(Seq("Belgium", "Antwerp", "Antwerp", "Essen"))))

    // Baarle Nassau
    analyzer.locate("51.43948683099483", "4.931525588035583") should equal(Some(Location(Seq("Netherlands", "North Brabant", "Baarle-Nassau"))))

    // Baarle-Hertog
    analyzer.locate("51.43581846832453", "4.926767349243164") should equal(Some(Location(Seq("Belgium", "Antwerp", "Turnhout", "Baarle-Hertog"))))

  }

}
