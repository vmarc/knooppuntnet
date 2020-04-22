package kpn.core.util

class UtilTest extends UnitTest {

  test("split") {

    Util.split(0, Seq(1, 2, 0, 3, 4, 5, 6, 0, 7, 8)) should equal(Seq(Seq(1, 2), Seq(3, 4, 5, 6), Seq(7, 8)))
    //Util.split(0, Seq(1, 2, 0, 3, 4, 5, 6, 0, 7, 8, 0)) should equal (Seq(Seq(1, 2), Seq(3, 4, 5, 6), Seq(7, 8)))
    Util.split(0, Seq(1, 2, 0, 0, 3, 4)) should equal(Seq(Seq(1, 2), Seq(), Seq(3, 4)))
  }

  test("withoutSuccessiveDuplicates") {
    Util.withoutSuccessiveDuplicates(Seq()) should equal(Seq())
    Util.withoutSuccessiveDuplicates(Seq(1)) should equal(Seq(1))
    Util.withoutSuccessiveDuplicates(Seq(1, 2)) should equal(Seq(1, 2))
    Util.withoutSuccessiveDuplicates(Seq(1, 1, 1, 2, 2, 3, 3, 4, 5, 5, 5)) should equal(Seq(1, 2, 3, 4, 5))
  }
}
