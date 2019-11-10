package kpn.server.json

import kpn.api.custom.Timestamp
import org.scalatest.FunSuite
import org.scalatest.Matchers

class TimestampJsonSerializerTest extends FunSuite with Matchers {

  test("serializer") {
    val timestamp = Timestamp(2018, 8, 11, 12, 34, 56)
    Json.string(timestamp) should equal(""""2018-08-11T12:34:56Z"""")
  }
}
