package kpn.server.json

import kpn.shared.Timestamp
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TimestampJsonDeserializerTest extends FunSuite with Matchers {

  test("deserializer") {
    val timestamp = Json.value(""""2018-08-11T12:34:56Z"""", classOf[Timestamp])
    timestamp should equal(Timestamp(2018, 8, 11, 12, 34, 56))
  }
}
