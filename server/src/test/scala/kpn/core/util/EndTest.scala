package kpn.core.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class EndTest extends AnyFunSuite with Matchers {

  case class Endable(name: String, end: Option[Boolean] = None)

  test("map to collection of enables") {
    val collection = Seq("1", "2", "3")

    val result: Seq[Endable] = End.map(collection) { case(element, end) => Endable(element, end) }

    result should equal(Seq(Endable("1"), Endable("2"), Endable("3", Some(true))))
  }
}
