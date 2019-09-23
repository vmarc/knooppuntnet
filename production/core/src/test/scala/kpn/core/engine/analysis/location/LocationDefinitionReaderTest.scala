package kpn.core.engine.analysis.location

import java.io.File

import kpn.shared.DE
import kpn.shared.EN
import kpn.shared.FR
import kpn.shared.NL
import org.scalatest.FunSuite
import org.scalatest.Matchers

class LocationDefinitionReaderTest extends FunSuite with Matchers {

  test("Essen") {
    val file = new File("/kpn/conf/locations-2/Belgium_52411_AL2/Antwerp_53114_AL6/Antwerp_1902793_AL7/Essen_964003_AL8.GeoJson")
    val locationDefinition = new LocationDefinitionReader(file).read(Seq.empty)
    locationDefinition.name should equal("Essen")

    val locator = LocationLocator.from(locationDefinition)

    // Essen
    locator.contains("51.46774", "4.46839") should equal(true)

    // Baarle Nassau
    locator.contains("51.43948683099483", "4.931525588035583") should equal(false)
  }

  test("Belgium") {
    val file = new File("/kpn/conf/locations-2/Belgium_52411_AL2.GeoJson")
    val locationDefinition = new LocationDefinitionReader(file).read(Seq.empty)
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
