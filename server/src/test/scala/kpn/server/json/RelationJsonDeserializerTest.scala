package kpn.server.json

import kpn.api.common.data.NodeMember
import kpn.api.common.data.RelationMember
import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.api.custom.Tag
import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest

class RelationJsonDeserializerTest extends UnitTest with SharedTestObjects {

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
        NodeMember(node, Some("role1")),
        WayMember(way, None),
        RelationMember(subRelation, None),
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
