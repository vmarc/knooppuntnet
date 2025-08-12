package kpn.server.json

import kpn.api.common.Relation
import kpn.api.common.data.Member
import kpn.api.custom.Tag
import kpn.core.test.TestObjects.newNode
import kpn.core.test.TestObjects.newRelation
import kpn.core.test.TestObjects.newWay
import kpn.core.util.UnitTest

class RelationJsonDeserializerTest extends UnitTest {

  test("deserializer") {

    val node = newNode(
      tags = Seq(
        Tag("key1", "value1"),
        Tag("key2", "value2"),
        Tag("key3", "value3"),
      )
    )

    val way = newWay(
      101,
      tags = Seq(
        Tag("key1", "value1"),
        Tag("key2", "value2"),
        Tag("key3", "value3"),
      )
    )

    val subRelation = newRelation(
      2,
      tags = Seq(
        Tag("key1", "value1"),
        Tag("key2", "value2"),
        Tag("key3", "value3"),
      )
    )

    val relation1 = newRelation(
      1,
      members = Seq(
        Member(node = Some(node), role = Some("role1")),
        Member(way = Some(way)),
        Member(relation = Some(subRelation)),
      ),
      tags = Seq(
        Tag("key1", "value1"),
        Tag("key2", "value2"),
        Tag("key3", "value3"),
      )
    )

    val json = Json.string(relation1)
    val relation2 = Json.value(json, classOf[Relation])
    assertEqual(relation2, relation1)
  }
}
