package kpn.core.tools.typescript

import kpn.core.util.UnitTest

class CamelCaseUtilTest extends UnitTest {

  test("case class name") {
    CamelCaseUtil.toDashed("FunSuiteTest") should equal("fun-suite-test")
  }

  test("all uppercase") {
    CamelCaseUtil.toDashed("TEST") should equal("test")
  }

  test("uppercase words") {
    CamelCaseUtil.toDashed("textWITHSomeALLUppercaseWORDS") should equal("text-with-some-all-uppercase-words")
  }

}
