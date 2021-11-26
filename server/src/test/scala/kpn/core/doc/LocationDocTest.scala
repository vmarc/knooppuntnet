package kpn.core.doc

import kpn.api.common.DE
import kpn.api.common.NL
import kpn.core.util.UnitTest
import kpn.server.json.Json

class LocationDocTest extends UnitTest {

  test("json") {
    val names = Seq(
      LocationName(NL, "Frankrijk"),
      LocationName(DE, "Frankreich")
    )
    val doc = LocationDoc("fr", Seq.empty, "France", names)
    val json = Json.string(doc)
    Json.value(json, classOf[LocationDoc]) should equal(doc)
  }
}
