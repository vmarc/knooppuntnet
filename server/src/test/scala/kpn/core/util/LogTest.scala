package kpn.core.util

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class LogTest extends AnyFunSuite with Matchers {

  test("mock log messages") {

    val log = Log.mock

    log.info("one")

    val returnValue = Log.context("context-1") {
      Log.context("context-2") {
        log.debug("two")
        "test"
      }
    }

    log.error("three")

    val expected = Seq(
      "INFO one",
      "DEBUG [context-1, context-2] two",
      "ERROR three"
    )

    log.messages should equal(expected)
    returnValue should equal("test")
  }
}
