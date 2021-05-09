package kpn.server.json

import kpn.api.custom.Day
import kpn.core.util.UnitTest

class DayJsonTest extends UnitTest {

  test("serializer") {
    Json.string(Day(2020, 8, Some(11))) should equal(""""2020-08-11"""")
    Json.string(Day(2020, 8, None)) should equal(""""2020-08"""")
  }

  test("deserializer") {
    Json.value(""""2020-08-11"""", classOf[Day]) should equal(Day(2020, 8, Some(11)))
    Json.value(""""2020-08"""", classOf[Day]) should equal(Day(2020, 8, None))
  }

  test("deserializer - backward compatibility") {
    Json.value("""{"year":2020,"month":8,"day":11}""", classOf[Day]) should equal(Day(2020, 8, Some(11)))
    Json.value("""{"year":2020,"month":8}""", classOf[Day]) should equal(Day(2020, 8, None))
  }

  test("values should be int") {
    val day = Json.value(""""2020-08-11"""", classOf[Day])
    day.year shouldBe a[Int]
    day.month shouldBe a[Int]
    day.day.get shouldBe a[Int]
  }
}
