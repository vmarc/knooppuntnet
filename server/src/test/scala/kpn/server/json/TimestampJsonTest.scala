package kpn.server.json

import kpn.api.custom.Timestamp
import kpn.core.util.UnitTest

class TimestampJsonTest extends UnitTest {

  test("serializer") {
    val timestamp = Timestamp(2018, 8, 11, 12, 34, 56)
    Json.string(timestamp) should equal(""""2018-08-11T12:34:56Z"""")
  }

  test("deserializer") {
    val timestamp = Json.value(""""2018-08-11T12:34:56Z"""", classOf[Timestamp])
    timestamp should equal(Timestamp(2018, 8, 11, 12, 34, 56))
  }
}
