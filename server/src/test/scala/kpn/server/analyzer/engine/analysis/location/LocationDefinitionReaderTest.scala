package kpn.server.analyzer.engine.analysis.location

import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.NL
import kpn.core.util.UnitTest

class LocationDefinitionReaderTest extends UnitTest {

  test("Essen") {
    val file = LocationConfigurationDefinition.file("be/Essen_964003_AL8")
    val locationDefinition = new LocationDefinitionReader(file).read()
    locationDefinition.name should equal("Essen")

    val locator = LocationLocator.from(locationDefinition)

    // Essen
    locator.contains("51.46774", "4.46839") should equal(true)

    // Baarle Nassau
    locator.contains("51.43948683099483", "4.931525588035583") should equal(false)
  }

  test("Belgium") {
    val file = LocationConfigurationDefinition.file("be/Belgium_52411_AL2")
    val locationDefinition = new LocationDefinitionReader(file).read()
    locationDefinition.name should equal("Belgium")
    locationDefinition.name(NL) should equal("België")
    locationDefinition.name(EN) should equal("Belgium")
    locationDefinition.name(DE) should equal("Belgien")
    locationDefinition.name(FR) should equal("Belgique")

    val locator = LocationLocator.from(locationDefinition)

    // Essen
    locator.contains("51.46774", "4.46839") should equal(true)

    // outer 1 Baarle Nassau
    locator.contains("51.43581846832453", "4.926767349243164") should equal(true)

    // outer 2 Baarle Nassau
    locator.contains("51.43563788497879", "4.941433668136596") should equal(true)

    // inner in be outer Baarle Nassau
    locator.contains("51.43948683099483", "4.931525588035583") should equal(false)

  }
}
