package kpn.tools.code.codecs

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.json.Json
import org.locationtech.jts.geom.Coordinate

import java.io.StringReader

class CoordinateArrayTest extends UnitTest {

  test("encode and decode coordinate array") {

    val coordinateArray = TestObject(CoordinateArray(Array(new Coordinate(1.1, 2.2), new Coordinate(3.3, 4.4))))
    //    val coordinateArray = CoordinateArray(Array(new Coordinate(1.1, 2.2), new Coordinate(3.3, 4.4)))

    val json = Json.string(coordinateArray)

    //json should equal("""{"obj": "[[1.1,2.2],[3.3,4.4]]"}""")
    json should equal("""{"obj": "[[1.1,2.2],[3.3,4.4]]"}""")

    val reader = new StringReader(json)
    val decoded = Json.readValue(reader, classOf[TestObject[CoordinateArray]])

    println()
    //    coordinateArray.coordinates.length should equal(decoded.coordinates.length)
    //    coordinateArray.coordinates.head.x should equal(decoded.coordinates.head.x)
    //    coordinateArray.coordinates.head.y should equal(decoded.coordinates.head.y)
    //    coordinateArray.coordinates(1).x should equal(decoded.coordinates(1).x)
    //    coordinateArray.coordinates(1).y should equal(decoded.coordinates(1).y)
  }
}
