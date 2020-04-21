package kpn.core.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TripletTest extends AnyFunSuite with Matchers {

  test("collection") {

    val collection = Seq(1, 2, 3, 4)

    Triplet.slide(collection) should equal(Seq(
      Triplet(None, 1, Some(2)),
      Triplet(Some(1), 2, Some(3)),
      Triplet(Some(2), 3, Some(4)),
      Triplet(Some(3), 4, None)
    ))
  }

  test("collection with just 1 element") {
    val collection = Seq(1)
    Triplet.slide(collection) should equal(Seq(
      Triplet(None, 1, None)
    ))
  }

  test("collection with two elements") {
    val collection = Seq(1, 2)
    Triplet.slide(collection) should equal(Seq(
      Triplet(None, 1, Some(2)),
      Triplet(Some(1), 2, None)
    ))
  }
}
