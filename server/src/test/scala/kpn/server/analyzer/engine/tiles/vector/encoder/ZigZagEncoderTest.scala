package kpn.server.analyzer.engine.tiles.vector.encoder

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ZigZagEncoderTest extends AnyFunSuite with Matchers {

  test("ZigZagEncode") {
    // https://developers.google.com/protocol-buffers/docs/encoding#types
    ZigZagEncoder.encode(0) should equal(0)
    ZigZagEncoder.encode(-1) should equal(1)
    ZigZagEncoder.encode(1) should equal(2)
    ZigZagEncoder.encode(-2) should equal(3)
  }

}
