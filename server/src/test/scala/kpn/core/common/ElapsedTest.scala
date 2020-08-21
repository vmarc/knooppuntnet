package kpn.core.common

import kpn.core.util.UnitTest

class ElapsedTest extends UnitTest {

  test("elapsed") {
    Elapsed.string(3 * 1000) should equal("3 seconds")
    Elapsed.string(((6 * 60) + 12) * 1000) should equal("6 minutes, 12 seconds")
    Elapsed.string(((14 * 60 * 60) + (6 * 60) + 12) * 1000) should equal("14 hours, 6 minutes")
    Elapsed.string(((3 * 60 * 60 * 24) + (14 * 60 * 60) + (6 * 60) + 12) * 1000) should equal("3 days, 14 hours, 6 minutes")
  }

}
