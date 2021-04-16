package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.NL
import kpn.api.custom.Country
import kpn.core.util.UnitTest

class LocationDefinitionReaderTest extends UnitTest {

  test("location definition reader") {

    val locationDefinitions = new LocationDefinitionReader(LocationConfigurationDefinition.DIR, Country.be).read()

    testEssen(locationDefinitions)
    testBelgium(locationDefinitions)
  }

  private def testEssen(locationDefinitions: Seq[LocationDefinition]): Unit = {

    val locationDefinition = locationDefinitions.find(_.name == "Essen").get

    locationDefinition.name(NL) should equal("Essen")
    locationDefinition.name(EN) should equal("Essen")
    locationDefinition.name(DE) should equal("Essen")
    locationDefinition.name(FR) should equal("Essen")

    val locator = LocationLocator.from(locationDefinition)

    // Essen
    assert(locator.contains("51.46774", "4.46839"))

    // Baarle Nassau
    assert(!locator.contains("51.43948683099483", "4.931525588035583"))
  }

  private def testBelgium(locationDefinitions: Seq[LocationDefinition]): Unit = {

    val locationDefinition = locationDefinitions.find(_.name == "Belgium").get

    locationDefinition.name(NL) should equal("België")
    locationDefinition.name(EN) should equal("Belgium")
    locationDefinition.name(DE) should equal("Belgien")
    locationDefinition.name(FR) should equal("Belgique")

    val locator = LocationLocator.from(locationDefinition)

    // Essen
    assert(locator.contains("51.46774", "4.46839"))

    // outer 1 Baarle Nassau
    assert(locator.contains("51.43581846832453", "4.926767349243164"))

    // outer 2 Baarle Nassau
    assert(locator.contains("51.43563788497879", "4.941433668136596"))

    // inner in be outer Baarle Nassau
    assert(!locator.contains("51.43948683099483", "4.931525588035583"))
  }
}
