package kpn.core.tools.country

import kpn.core.util.UnitTest

class RingBuilderTest extends UnitTest {

  test("no rings when no ways") {
    RingBuilder.findRings(Seq()) shouldBe empty
  }

  test("two rings consisting of multiple ways") {
    val ways: Seq[SkeletonWay] = Seq(
      SkeletonWay(1, Seq(101, 102)),
      SkeletonWay(2, Seq(102, 103)),
      SkeletonWay(3, Seq(103, 104)),
      SkeletonWay(4, Seq(104, 101)),
      SkeletonWay(5, Seq(105, 106)),
      SkeletonWay(6, Seq(106, 107)),
      SkeletonWay(7, Seq(107, 108)),
      SkeletonWay(8, Seq(108, 105))
    )

    val rings = RingBuilder.findRings(ways)
    rings.map(_.ways.map(_.id)) should equal(Seq(Seq(1, 2, 3, 4), Seq(5, 6, 7, 8)))
  }

  test("two rings, each consisting of a single way") {
    val ways: Seq[SkeletonWay] = Seq(
      SkeletonWay(1, Seq(101, 102, 103, 101)),
      SkeletonWay(2, Seq(104, 105, 106, 104))
    )

    val rings = RingBuilder.findRings(ways)
    rings.map(_.ways.map(_.id)) should equal(Seq(Seq(1), Seq(2)))
  }

  test("one ring including a way that needs to be reversed to form the ring") {
    val ways: Seq[SkeletonWay] = Seq(
      SkeletonWay(1, Seq(101, 102)),
      SkeletonWay(2, Seq(103, 102)),
      SkeletonWay(3, Seq(103, 101))
    )

    val rings = RingBuilder.findRings(ways)
    rings.map(_.ways.map(_.id)) should equal(Seq(Seq(1, 2, 3)))
  }

  test("no connecting ways found") {
    val ways: Seq[SkeletonWay] = Seq(
      SkeletonWay(1, Seq(101, 102)),
      SkeletonWay(2, Seq(102, 103))
    )

    intercept[Exception] {
      RingBuilder.findRings(ways)
    }.getMessage should equal("no connecting ways found. rings=0, currentRing=2")
  }

  test("multiple connecting ways") {
    val ways: Seq[SkeletonWay] = Seq(
      SkeletonWay(1, Seq(101, 102)),
      SkeletonWay(2, Seq(102, 103)),
      SkeletonWay(3, Seq(102, 104))
    )

    intercept[Exception] {
      RingBuilder.findRings(ways)
    }.getMessage should equal("multiple connecting ways found; connecting way 1 with end node 102 to ways 2, 3")
  }
}
