package kpn.tools.code.codecs

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
import org.locationtech.jts.geom.Coordinate

import java.io.StringReader

class CoordinateTest extends UnitTest {

  test("encode and decode coordinate") {

    val coordinateArray = CoordinateArray(Array(new Coordinate(1.1, 2.2)))

    val json = Json.pretty(coordinateArray)

    assertEqual(
      json,
      """{
        |  "coordinates": [
        |    {
        |      "x": 1.1,
        |      "y": 2.2
        |    }
        |  ]
        |}""".stripMargin
    )

    val reader = new StringReader(json)
    val decoded = Json.readValue(reader, classOf[CoordinateArray])

    coordinateArray.coordinates.length should equal(decoded.coordinates.length)
    coordinateArray.coordinates.head.x should equal(decoded.coordinates.head.x)
    coordinateArray.coordinates.head.y should equal(decoded.coordinates.head.y)
  }
}
