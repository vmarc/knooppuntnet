package kpn.server.json.enumeratum

import kpn.api.common.data.MemberType
import kpn.core.util.UnitTest
import kpn.server.json.Json

case class TestClass(memberType: MemberType)

case class TestClass2(memberType: Option[MemberType])

class MemberTypeJsonTest extends UnitTest {

  test("serializer") {
    Json.string(MemberType.Node) should equal(""""node"""")
    Json.string(MemberType.Way) should equal(""""way"""")
    Json.string(MemberType.Relation) should equal(""""relation"""")
    Json.string(TestClass(MemberType.Node)) should equal("""{"memberType":"node"}""")
    Json.string(TestClass2(Some(MemberType.Node))) should equal("""{"memberType":"node"}""")
    Json.string(TestClass2(None)) should equal("""{}""")
  }

  test("deserializer") {
    Json.value(""""node"""", classOf[MemberType]) should equal(MemberType.Node)
    Json.value(""""way"""", classOf[MemberType]) should equal(MemberType.Way)
    Json.value(""""relation"""", classOf[MemberType]) should equal(MemberType.Relation)
    Json.value("""{"memberType":"node"}""", classOf[TestClass]) should equal(TestClass(MemberType.Node))
    Json.value("""{"memberType":"node"}""", classOf[TestClass2]) should equal(TestClass2(Some(MemberType.Node)))
    Json.value("""{}""", classOf[TestClass2]) should equal(TestClass2(None))

    intercept[NoSuchElementException] {
      Json.value(""""bla"""", classOf[MemberType])
    }.getMessage should equal("bla is not a member of Enum (node, way, relation)")
  }
}
