package kpn.core.tools.translations.domain

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TrimTest extends AnyFunSuite with Matchers {
  test("trim") {
    Trim.trim("  a   b   \n c ") should equal("a b c")
  }
}
