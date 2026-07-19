package kpn.core.doc

import kpn.core.util.UnitTest

class LocationPathTest extends UnitTest {

  test("contains") {
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq("1"))) should equal(true)
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq("1", "2"))) should equal(true)
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq("1", "2", "3"))) should equal(true)
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq("1", "2", "3", "4"))) should equal(false)
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq())) should equal(false)
    LocationPath(Seq("1", "2", "3")).contains(LocationPath(Seq("2"))) should equal(false)
  }
}
