package kpn.core.util

class UtilTest extends UnitTest {

  test("split") {

    Util.split(0, Seq(1, 2, 0, 3, 4, 5, 6, 0, 7, 8)) should equal(Seq(Seq(1, 2), Seq(3, 4, 5, 6), Seq(7, 8)))
    //Util.split(0, Seq(1, 2, 0, 3, 4, 5, 6, 0, 7, 8, 0)) should equal (Seq(Seq(1, 2), Seq(3, 4, 5, 6), Seq(7, 8)))
    Util.split(0, Seq(1, 2, 0, 0, 3, 4)) should equal(Seq(Seq(1, 2), Seq.empty, Seq(3, 4)))
  }

  test("withoutSuccessiveDuplicates") {
    Util.withoutSuccessiveDuplicates(Seq.empty) shouldBe empty
    Util.withoutSuccessiveDuplicates(Seq(1)) should equal(Seq(1))
    Util.withoutSuccessiveDuplicates(Seq(1, 2)) should equal(Seq(1, 2))
    Util.withoutSuccessiveDuplicates(Seq(1, 1, 1, 2, 2, 3, 3, 4, 5, 5, 5)) should equal(Seq(1, 2, 3, 4, 5))
  }

  test("humanReadableBytes") {
    Util.humanReadableBytes(1000) should equal("1000")
    Util.humanReadableBytes(1024) should equal("1.0K")
    Util.humanReadableBytes((2 * 1024) + (1024 / 2)) should equal("2.5K")
    Util.humanReadableBytes(1024 * 1024) should equal("1.0M")
    Util.humanReadableBytes(1024 * 1024 * 1024) should equal("1.0G")
  }
}
