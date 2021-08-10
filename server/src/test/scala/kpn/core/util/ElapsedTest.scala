package kpn.core.util

class ElapsedTest extends UnitTest {

  test("formatting elapsed time") {
    Elapsed(300) should equal("0.300s")
    Elapsed(6000 + 300) should equal("6.300s")
    Elapsed((12 * 60 * 1000) + 6000 + 300) should equal("12:06")
    Elapsed((2 * 60 * 60 * 1000) + (12 * 60 * 1000) + 6000 + 300) should equal("2:12:06")
  }
}
