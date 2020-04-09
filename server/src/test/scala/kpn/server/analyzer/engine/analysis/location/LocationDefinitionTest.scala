package kpn.server.analyzer.engine.analysis.location

import org.locationtech.jts.geom.Envelope
import org.scalatest.FunSuite
import org.scalatest.Matchers

class LocationDefinitionTest extends FunSuite with Matchers {

  test("get") {

    val root = locationDefinition(
      "root",
      Seq(
        locationDefinition(
          "1",
          Seq(
            locationDefinition("11", Seq.empty),
              locationDefinition("12", Seq.empty),
              locationDefinition("13", Seq.empty)
          )
        ),
        locationDefinition(
          "2",
          Seq(
            locationDefinition("21", Seq.empty),
            locationDefinition("22", Seq.empty),
            locationDefinition(
              "33",
              Seq(
                locationDefinition("331", Seq.empty),
                locationDefinition("332", Seq.empty),
                locationDefinition("333", Seq.empty)
              )
            )
          )
        )
      )
    )

    LocationDefinition.find(root, "root").map(_.name) should equal(Some("root"))
    LocationDefinition.find(root, "1").map(_.name) should equal(Some("1"))
    LocationDefinition.find(root, "13").map(_.name) should equal(Some("13"))
    LocationDefinition.find(root, "333").map(_.name) should equal(Some("333"))
    LocationDefinition.find(root, "4").map(_.name) should equal(None)
  }

  private def locationDefinition(name: String, children: Seq[LocationDefinition]): LocationDefinition = {
    LocationDefinition(
      id = "",
      name = name,
      locationNames = Map.empty,
      filename = "",
      boundingBox = new Envelope(),
      geometry = null,
      children = children
    )
  }

}
