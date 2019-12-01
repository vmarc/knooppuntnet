package kpn.server.analyzer.engine.tiles.vector.encoder

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import kpn.server.analyzer.engine.tiles.vector.ProtobufVectorTile
import org.scalatest.FunSuite
import org.scalatest.Matchers

class GeometryTypeEncoderTest extends FunSuite with Matchers {

  test("encode") {
    val cs = Array(
      new Coordinate(3, 6),
      new Coordinate(8, 12),
      new Coordinate(20, 34)
    )
    val gf = new GeometryFactory()
    val geometry = gf.createLineString(cs)

    GeometryTypeEncoder.encode(geometry) should equal(ProtobufVectorTile.Tile.LINESTRING)
  }
}
