package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.location.Location
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeLocationAnalyzerTest extends FunSuite with Matchers {

  test("NodeLocationAnalyzer") {

    val analyzer = {
      val configuration = new LocationConfigurationReader().read()
      new NodeLocationAnalyzerImpl(configuration)
    }

    // Essen
    analyzer.locate("51.46774", "4.46839") should equal(Some(Location(Seq("be", "Flanders", "Antwerp province", "Antwerp arrondissement", "Essen BE"))))

    // Baarle Nassau
    analyzer.locate("51.43948683099483", "4.931525588035583") should equal(Some(Location(Seq("nl", "North Brabant", "Baarle-Nassau"))))

    // Baarle-Hertog
    analyzer.locate("51.43581846832453", "4.926767349243164") should equal(Some(Location(Seq("be", "Flanders", "Antwerp province", "Turnhout arrondissement", "Baarle-Hertog"))))

  }

}
