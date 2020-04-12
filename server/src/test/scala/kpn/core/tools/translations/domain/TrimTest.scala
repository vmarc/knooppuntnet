package kpn.core.tools.translations.domain

import org.scalatest.FunSuite
import org.scalatest.Matchers

class TrimTest extends FunSuite with Matchers {
  test("trim") {
    Trim.trim("  a   b   \n c ") should equal("a b c")
  }
}
