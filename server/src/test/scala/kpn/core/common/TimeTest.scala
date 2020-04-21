package kpn.core.common

import java.time.ZoneId
import java.time.ZonedDateTime

import kpn.api.custom.Timestamp
import org.scalatest.BeforeAndAfterEach
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class TimeTest extends AnyFunSuite with Matchers with BeforeAndAfterEach {

  override def afterEach(): Unit = {
    Time.clear()
  }

  test("default for now is to return the current system time") {
    assertSystemTime(Time.now)
  }

  test("now can be set explicitely") {
    val timestamp = Timestamp(2015, 11, 8, 12, 34, 56)
    Time.set(timestamp)
    Time.now should equal(timestamp)
  }

  test("Time can be cleared, after which current system time is returned again") {
    val timestamp = Timestamp(2015, 11, 8, 12, 34, 56)
    Time.set(timestamp)
    Time.clear()
    assertSystemTime(Time.now)
  }

  private def assertSystemTime(timestamp: Timestamp): Unit = {
    val systemSeconds = System.currentTimeMillis() / 1000

    val epochSeconds = ZonedDateTime.of(
      timestamp.year,
      timestamp.month,
      timestamp.day,
      timestamp.hour,
      timestamp.minute,
      timestamp.second,
      0,
      ZoneId.of("UTC")
    ).toInstant.toEpochMilli / 1000

    if (scala.math.abs(systemSeconds - epochSeconds) > 10L) {
      fail("time now is not current system time")
    }
  }
}
