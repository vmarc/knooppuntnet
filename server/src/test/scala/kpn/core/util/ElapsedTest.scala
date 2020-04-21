package kpn.core.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class ElapsedTest extends AnyFunSuite with Matchers {

  test("formatting elapsed time") {
    Elapsed(300) should equal("300ms")
    Elapsed(6000 + 300) should equal("6s, 300ms")
    Elapsed((12 * 60 * 1000) + 6000 + 300) should equal("12mins, 6s")
    Elapsed((2 * 60 * 60 * 1000) + (12 * 60 * 1000) + 6000 + 300) should equal("2hrs, 12mins, 6s")
  }
}
