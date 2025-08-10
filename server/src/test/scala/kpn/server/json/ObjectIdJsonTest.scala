package kpn.server.json

import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorGroup
import org.bson.types.ObjectId

class ObjectIdJsonTest extends UnitTest {

  test("serializer") {
    val objectId = new ObjectId("62fb7acddbaed5cc66a8f4de")
    Json.string(objectId) should equal("""{"$oid":"62fb7acddbaed5cc66a8f4de"}""")
  }

  test("deserializer") {
    val objectId = Json.value("""{"$oid":"62fb7acddbaed5cc66a8f4de"}""", classOf[ObjectId])
    objectId should equal(new ObjectId("62fb7acddbaed5cc66a8f4de"))
  }

  test("serializer and deserializer in action in object") {
    val group = MonitorGroup(new ObjectId("62fb7acddbaed5cc66a8f4de"), "name", "description")
    val json = Json.string(group)
    val deserialized = Json.value(json, classOf[MonitorGroup])
    deserialized should equal(group)
  }

  test("serializer and deserializer in action in new object") {
    val group = MonitorGroup(new ObjectId(), "name", "description")
    val json = Json.string(group)
    val deserialized = Json.value(json, classOf[MonitorGroup])
    deserialized should equal(group)
  }
}
