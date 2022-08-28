package kpn.api.custom

import kpn.core.util.UnitTest

class DayTest extends UnitTest {

  test("fromString") {

    Day.fromString("2020-08-11") should equal(Some(Day(2020, 8, 11)))
    Day.fromString("2020-08") should equal(Some(Day(2020, 8)))

    Day.fromString("bla") should equal(None)
    Day.fromString("202a-08-11") should equal(None)
    Day.fromString("2020-0a-11") should equal(None)
    Day.fromString("2020-08-1a") should equal(None)
  }

  test("yyyymmdd") {
    Day(2020, 8, 11).yyyymmdd should equal("2020-08-11")
    Day(2020, 8, None).yyyymmdd should equal("2020-08")
  }
}
