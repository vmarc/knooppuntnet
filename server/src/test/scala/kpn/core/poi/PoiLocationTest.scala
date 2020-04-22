package kpn.core.poi

import kpn.core.util.UnitTest

class PoiLocationTest extends UnitTest {

  test("bounding boxes") {
    PoiLocation.boundingBoxStrings should equal(
      Seq(
        // "(50.2893, 5.625, 51.618, 9.1406)",
        "(49.153, 2.4609, 53.3309, 7.3828)"
      )
    )
  }

}
