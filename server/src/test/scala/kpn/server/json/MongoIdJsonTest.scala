package kpn.server.json

import kpn.api.base.MongoId
import kpn.api.common.monitor.MonitorGroup
import kpn.core.util.UnitTest

class MongoIdJsonTest extends UnitTest {

  test("serializer") {
    val mongoId = MongoId("62fb7acddbaed5cc66a8f4de")
    Json.string(mongoId) should equal("""{"$oid":"62fb7acddbaed5cc66a8f4de"}""")
  }

  test("deserializer") {
    val mongoId = Json.value("""{"$oid":"62fb7acddbaed5cc66a8f4de"}""", classOf[MongoId])
    mongoId should equal(MongoId("62fb7acddbaed5cc66a8f4de"))
  }

  test("serializer and deserializer in action in object") {
    val group = MonitorGroup(MongoId("62fb7acddbaed5cc66a8f4de"), "name", "description")
    val json = Json.string(group)
    val deserialized = Json.value(json, classOf[MonitorGroup])
    deserialized should equal(group)
  }

  test("serializer and deserializer in action in new object") {
    val group = MonitorGroup(MongoId(), "name", "description")
    val json = Json.string(group)
    val deserialized = Json.value(json, classOf[MonitorGroup])
    deserialized should equal(group)
  }
}
