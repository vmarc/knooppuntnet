package kpn.core.util

import org.scalatest.exceptions.StackDepthException
import org.scalatest.exceptions.TestFailedException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

abstract class UnitTest extends AnyFunSuite with Matchers {

  def assertEqual(actual: Object, expected: Object): Unit = {
    if (actual != expected) {
      val jsonActual = pprint.apply(actual)
      val jsonExpected = pprint.apply(expected)
      throw new TestFailedException(
        (e: StackDepthException) => Some(s"""$jsonActual did not equal $jsonExpected"""),
        None,
        (e: StackDepthException) => 1, // source line of call point assertEqual() in failure message
      )
    }
  }

  def pendingRedesignPrio2(): Unit = {
    pending
  }

  def pendingRedesign(): Unit = {
    pending
  }

  def pendingRedesignImplementation(): Unit = {
    pending
  }

  def pendingRedesignNonAnalysis(): Unit = {
    pending
  }

  def pendingRedesignLoop(): Unit = {
    pending
  }

  def pendingRedesignRoutePlanning(): Unit = {
    pending
  }
}
