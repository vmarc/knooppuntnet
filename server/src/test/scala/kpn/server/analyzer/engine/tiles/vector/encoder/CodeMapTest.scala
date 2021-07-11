package kpn.server.analyzer.engine.tiles.vector.encoder

import kpn.core.util.UnitTest

class CodeMapTest extends UnitTest {

  test("empty map") {
    val codeMap = new CodeMap()
    codeMap.keys should equal(Seq.empty)
  }

  test("encode values") {
    val codeMap = new CodeMap()
    codeMap.code("one") should equal(0)
    codeMap.code("two") should equal(1)
    codeMap.code("three") should equal(2)
    codeMap.code("four") should equal(3)
    codeMap.code("one") should equal(0)
    codeMap.code("two") should equal(1)
    codeMap.code("three") should equal(2)
    codeMap.code("four") should equal(3)

    codeMap.keys should equal(Seq("one", "two", "three", "four"))
  }

}
