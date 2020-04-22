package kpn.server.analyzer.engine.tiles.vector.encoder

import kpn.core.util.UnitTest

class ZigZagEncoderTest extends UnitTest {

  test("ZigZagEncode") {
    // https://developers.google.com/protocol-buffers/docs/encoding#types
    ZigZagEncoder.encode(0) should equal(0)
    ZigZagEncoder.encode(-1) should equal(1)
    ZigZagEncoder.encode(1) should equal(2)
    ZigZagEncoder.encode(-2) should equal(3)
  }

}
