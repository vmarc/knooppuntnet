package kpn.api.custom

import kpn.core.util.UnitTest

class TimestampTest extends UnitTest {

  test("<") {
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2016, 5, 5, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 6, 5, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 6, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 6, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 5, 6, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 5, 5, 6))

    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2014, 5, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 4, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 4, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 4, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 5, 4, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) < Timestamp(2015, 5, 5, 5, 5, 4)))
  }

  test(">") {
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2014, 5, 5, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 4, 5, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 4, 5, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 4, 5, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 5, 4, 5))
    assert(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 5, 5, 4))

    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2016, 5, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 6, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 6, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 6, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 5, 6, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) > Timestamp(2015, 5, 5, 5, 5, 6)))
  }

  test("===") {
    assert(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 5, 5, 5, 5, 5))

    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2014, 5, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 4, 5, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 5, 4, 5, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 5, 5, 4, 5, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 5, 5, 5, 4, 5)))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) === Timestamp(2015, 5, 5, 5, 5, 4)))
  }

  test(">=") {
    assert(Timestamp(2015, 5, 5, 5, 5, 5) >= Timestamp(2015, 5, 5, 5, 5, 5))
    assert(Timestamp(2016, 5, 5, 5, 5, 5) >= Timestamp(2015, 5, 5, 5, 5, 5))
    assert(!(Timestamp(2015, 5, 5, 5, 5, 5) >= Timestamp(2016, 5, 5, 5, 5, 5)))
  }

  test("compareTo") {
    Timestamp(2015, 5, 5, 5, 5, 5).compareTo(Timestamp(2015, 5, 5, 5, 5, 5)) should equal(0)
    Timestamp(2015, 5, 5, 5, 5, 5).compareTo(Timestamp(2014, 5, 5, 5, 5, 5)) should equal(1)
    Timestamp(2015, 5, 5, 5, 5, 5).compareTo(Timestamp(2016, 5, 5, 5, 5, 5)) should equal(-1)
  }

  test("ordering") {
    Seq(
      Timestamp(2015, 1, 1, 1, 1, 1),
      Timestamp(2015, 1, 1, 1, 1, 5),
      Timestamp(2015, 1, 1, 1, 1, 4),
      Timestamp(2015, 1, 1, 1, 1, 3),
      Timestamp(2015, 1, 1, 1, 1, 2)
    ).sorted.map(_.second) should equal(Seq(1, 2, 3, 4, 5))
  }

  test("formatting") {

    val timestamp = Timestamp(2015, 1, 2, 3, 4, 5)

    timestamp.yyyymmdd should equal("2015-01-02")
    timestamp.hhmmss should equal("03:04:05")
    timestamp.yyyymmddhhmmss should equal("2015-01-02 03:04:05")
    timestamp.yyyymmddhhmm should equal("2015-01-02 03:04")
    timestamp.yearString should equal("2015")
    timestamp.monthString should equal("01")
    timestamp.dayString should equal("02")
    timestamp.hourString should equal("03")
    timestamp.minuteString should equal("04")
    timestamp.secondString should equal("05")
    timestamp.iso should equal("2015-01-02T03:04:05Z")
  }

  test("apply") {
    Timestamp(2015, 1, 2) should equal(Timestamp(2015, 1, 2, 0, 0, 0))
  }
}
