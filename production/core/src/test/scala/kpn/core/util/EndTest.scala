package kpn.core.util

import org.scalatest.FunSuite
import org.scalatest.Matchers

class EndTest extends FunSuite with Matchers {

  case class Endable(name: String, end: Option[Boolean] = None)

  test("map to collection of enables") {
    val collection = Seq("1", "2", "3")

    val result: Seq[Endable] = End.map(collection) { case(element, end) => Endable(element, end) }

    result should equal(Seq(Endable("1"), Endable("2"), Endable("3", Some(true))))
  }
}
