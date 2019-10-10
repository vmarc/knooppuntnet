package kpn.core.tiles.vector.encoder

import org.scalatest.FunSuite
import org.scalatest.Matchers

class ZigZagEncoderTest extends FunSuite with Matchers {

  test("ZigZagEncode") {
    // https://developers.google.com/protocol-buffers/docs/encoding#types
    ZigZagEncoder.encode(0) should equal(0)
    ZigZagEncoder.encode(-1) should equal(1)
    ZigZagEncoder.encode(1) should equal(2)
    ZigZagEncoder.encode(-2) should equal(3)
  }

}
