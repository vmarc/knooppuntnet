package kpn.core.util

import kpn.server.json.Json
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

abstract class UnitTest extends AnyFunSuite with Matchers {

  def assertEqual(object1: Object, object2: Object): Unit = {
    if (object1 != object2) {
      val json1 = Json.pretty(object1)
      val json2 = Json.pretty(object2)
      throw new TestFailedException(
        (e: StackDepthException) => Some(s"""$json1 did not equal $json2"""),
        None,
        (e: StackDepthException) => 1, // source line of call point assertEqual() in failure message
      )
    }
  }
}
