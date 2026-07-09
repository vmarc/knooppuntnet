package kpn.core.poi

import kpn.core.util.UnitTest

class PoiLocationTest extends UnitTest {

  test("bounding boxes") {
    PoiLocation.boundingBoxStrings.head should equal("(49.3824, 2.4609, 53.5403, 7.3828)")
  }

}
