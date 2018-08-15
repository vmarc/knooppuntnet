package kpn.core.tiles.vector.encoder

import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.GeometryFactory
import kpn.core.tiles.vector.ProtobufVectorTile
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
