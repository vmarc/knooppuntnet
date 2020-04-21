package kpn.core.poi

import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class PoiLocationTest extends FunSuite with Matchers {

  test("bounding boxes") {
    PoiLocation.boundingBoxStrings should equal(
      Seq(
        // "(50.2893, 5.625, 51.618, 9.1406)",
        "(49.153, 2.4609, 53.3309, 7.3828)"
      )
    )
  }

}
