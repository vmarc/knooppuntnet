package kpn.core.tiles.vector.encoder

import com.vividsolutions.jts.algorithm.CGAlgorithms
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import org.scalatest.FunSuite
import org.scalatest.Matchers

class CommandEncoderTest extends FunSuite with Matchers {

  private val gf = new GeometryFactory()

  test("commands") { // Ex.: MoveTo(3, 6), LineTo(8, 12), LineTo(20, 34), ClosePath
    val coordinates = Seq(
      new Coordinate(3, 6),
      new Coordinate(8, 12),
      new Coordinate(20, 34)
    )

    val commands = new CommandEncoder().makeCommands2(coordinates, closePathAtEnd = true)
    commands should equal(Seq(9, 6, 12, 18, 10, 12, 24, 44, 15))
  }

  test("CommandsFilter") {
    val coordinates = Seq(
      new Coordinate(3, 6),
      new Coordinate(8, 12),
      new Coordinate(8, 12),
      new Coordinate(20, 34)
    )
    val commands = new CommandEncoder().makeCommands2(coordinates, closePathAtEnd = true)
    commands should equal(Seq(9, 6, 12, 18, 10, 12, 24, 44, 15))
  }

  test("Point") {
    val coordinates = Seq(
      new Coordinate(3, 6)
    )
    val commands = new CommandEncoder().makeCommands2(coordinates, closePathAtEnd = false)
    commands should equal(Seq(9, 6, 12))
  }

  test("MultiPoint") {
    val coordinates = Seq(
      new Coordinate(5, 7),
      new Coordinate(3, 2)
    )
    val commands = new CommandEncoder().makeCommands3(coordinates, closePathAtEnd = false, multiPoint = true)
    commands should equal(Seq(17, 10, 14, 3, 9))
  }

  test("CCWPolygon") { // Exterior ring in counter-clockwise order.
    val coordinates = Seq(
      new Coordinate(3, 6),
      new Coordinate(8, 12),
      new Coordinate(20, 34),
      new Coordinate(3, 6)
    )
    CGAlgorithms.isCCW(coordinates.toArray) should equal(true)
    val polygon = gf.createPolygon(coordinates.toArray)
    val commands = new CommandEncoder().makeCommands1(polygon)
    commands should equal(Seq(9, 6, 12, 18, 10, 12, 24, 44, 15))
  }

  test("CWPolygon") { // Exterior ring in clockwise order.
    val coordinates = Seq(
      new Coordinate(3, 6),
      new Coordinate(20, 34),
      new Coordinate(8, 12),
      new Coordinate(3, 6)
    )
    CGAlgorithms.isCCW(coordinates.toArray) should equal(false)
    val polygon = gf.createPolygon(coordinates.toArray)
    val commands = new CommandEncoder().makeCommands1(polygon)
    commands should equal(Seq(9, 6, 12, 18, 10, 12, 24, 44, 15))
  }

  test("ExtentWithoutScale") {
    val coordinates = Seq(
      new Coordinate(6, 300)
    )
    val commands = new CommandEncoder().makeCommands2(coordinates, closePathAtEnd = false)
    commands should equal(Seq(9, 12, 600))
  }

  test("FourEqualPoints") {
    val coordinates = Seq(
      new Coordinate(3, 6),
      new Coordinate(3, 6),
      new Coordinate(3, 6),
      new Coordinate(3, 6)
    )
    val commands = new CommandEncoder().makeCommands2(coordinates, closePathAtEnd = false)
    commands should equal(Seq(9, 6, 12))
  }
}
