package kpn.server.json

import kpn.api.base.MongoId
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
}
