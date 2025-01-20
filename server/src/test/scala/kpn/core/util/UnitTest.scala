package kpn.core.util

import kpn.server.json.Json
import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

abstract class UnitTest extends AnyFunSuite with Matchers {

  def assertEqual(actual: Object, expected: Object): Unit = {
    if (actual != expected) {
      val jsonActual = Json.pretty(actual)
      val jsonExpected = Json.pretty(expected)
      throw new TestFailedException(
        (e: StackDepthException) => Some(s"""$jsonExpected did not equal $jsonActual"""),
        None,
        (e: StackDepthException) => 1, // source line of call point assertEqual() in failure message
      )
    }
  }
}
