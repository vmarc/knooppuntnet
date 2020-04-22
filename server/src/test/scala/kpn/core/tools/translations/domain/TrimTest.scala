package kpn.core.tools.translations.domain

import kpn.core.util.UnitTest

class TrimTest extends UnitTest {
  test("trim") {
    Trim.trim("  a   b   \n c ") should equal("a b c")
  }
}
