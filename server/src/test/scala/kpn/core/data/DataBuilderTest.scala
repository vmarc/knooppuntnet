package kpn.core.data

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tags
import kpn.core.util.Log
import kpn.core.util.UnitTest

class DataBuilderTest extends UnitTest with SharedTestObjects {

  test("happy data build") {

    val node1 = newRawNode(101, tags = Tags.from("name" -> "01"))
    val node2 = newRawNode(102, tags = Tags.from("name" -> "02"))
    val node3 = newRawNode(103, tags = Tags.from("name" -> "03"))
    val node4 = newRawNode(104, tags = Tags.from("name" -> "04"))
    val node5 = newRawNode(105, tags = Tags.from("name" -> "05"))
    val node6 = newRawNode(106, tags = Tags.from("name" -> "06"))

    val way1 = newRawWay(11, nodeIds = Seq(103, 104))
    val way2 = newRawWay(12, nodeIds = Seq(105, 106))

    val relation1 = newRawRelation(1)
    val relation2 = newRawRelation(2)

    val membersRelation3 = Seq(
      RawMember("node", 101, None),
      RawMember("node", 102, Some("role1")),
      RawMember("way", 11, None),
      RawMember("way", 12, Some("role2")),
      RawMember("relation", 1, None),
      RawMember("relation", 2, Some("role2"))
    )

    val relation3 = newRawRelation(3, members = membersRelation3)

    val nodes: Seq[RawNode] = Seq(node1, node2, node3, node4, node5, node6)
    val ways: Seq[RawWay] = Seq(way1, way2)
    val relations: Seq[RawRelation] = Seq(relation1, relation2, relation3)

    val rawData = RawData(None, nodes, ways, relations)

    val log = Log.mock

    val data = new DataBuilder(rawData, log).data

    println(">>>>")
    println(log.messages)
    println("<<<<")

    log.messages.size should equal(0)

    data.nodes(101).tags("name") should equal(Some("01"))
    data.nodes(102).tags("name") should equal(Some("02"))
    data.nodes(103).tags("name") should equal(Some("03"))
    data.nodes(104).tags("name") should equal(Some("04"))
    data.nodes(105).tags("name") should equal(Some("05"))
    data.nodes(106).tags("name") should equal(Some("06"))

    data.ways(11).nodes.map(_.id) should equal(Seq(103, 104))
    data.ways(12).nodes.map(_.id) should equal(Seq(105, 106))

    data.relations(3).nodeMembers.map(_.node.id) should equal(Seq(101, 102))
    data.relations(3).wayMembers.map(_.way.id) should equal(Seq(11, 12))
    data.relations(3).relationMembers.map(_.relation.id) should equal(Seq(1, 2))
  }

  test("recursive reference to relation") {

    val relation1 = newRawRelation(
      1,
      members = Seq(
        RawMember("relation", 2, None)
      )
    )
    val relation2 = newRawRelation(
      2,
      members = Seq(
        RawMember("relation", 1, None)
      )
    )

    val relations: Seq[RawRelation] = Seq(relation1, relation2)

    val rawData = RawData(None, Seq(), Seq(), relations)

    val log = Log.mock

    val data = new DataBuilder(rawData, log).data

    val parsedRelation1 = data.relations(1)
    val parsedRelation2 = data.relations(2)

    parsedRelation1.id should equal(1)
    parsedRelation1.relationMembers.size should equal(1)
    parsedRelation1.relationMembers.head.relation.id should equal(2)
    parsedRelation1.relationMembers.head.relation.members.size should equal(0)

    parsedRelation2.id should equal(2)
    parsedRelation2.relationMembers.size should equal(0)

    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: relation 2 references parent relation 1 as member, continue processing without this reference")
  }

  test("way node missing") {

    val node = newRawNode(101)
    val way = newRawWay(10, nodeIds = Seq(101, 102))

    val rawData = RawData(None, Seq(node), Seq(way), Seq())

    val log = Log.mock

    val data = new DataBuilder(rawData, log).data

    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: node 102 (referenced from way 10) not found in data")
  }

  test("relation member node missing") {

    val relation = newRawRelation(
      1,
      members = Seq(
        RawMember("node", 101, None)
      )
    )

    val log = Log.mock

    val rawData = RawData(None, Seq(), Seq(), Seq(relation))
    val data = new DataBuilder(rawData, log).data

    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: node 101 (referenced from relation 1) not found in data")
  }

  test("relation member way missing") {
    val relation = newRawRelation(
      1,
      members = Seq(
        RawMember("way", 10, None)
      )
    )

    val log = Log.mock

    val rawData = RawData(None, Seq(), Seq(), Seq(relation))
    val data = new DataBuilder(rawData, log).data

    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: way 10 (referenced from relation 1) not found in data")
  }

  test("relation member relation missing") {
    val relation = newRawRelation(
      1,
      members = Seq(
        RawMember("relation", 2, None)
      )
    )

    val log = Log.mock

    val rawData = RawData(None, Seq(), Seq(), Seq(relation))
    val data = new DataBuilder(rawData, log).data

    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: relation 2 not found")
  }

  test("unknown relation member type") {
    val relation = newRawRelation(
      1,
      members = Seq(
        RawMember("bla", 11, None)
      )
    )

    val log = Log.mock

    val rawData = RawData(None, Seq(), Seq(), Seq(relation))
    val data = new DataBuilder(rawData, log).data

    log.messages.size should equal(1)
    log.messages.head should equal("""WARN data inconsistancy: unknown member type "bla" in relation 1""")
  }

  test("ignore relation self references") {

    val node1 = newRawNode(101, tags = Tags.from("name" -> "01"))
    val node2 = newRawNode(102)
    val node3 = newRawNode(103)
    val way = newRawWay(11, nodeIds = Seq(102, 103))

    val relation = newRawRelation(
      1, members = Seq(
        RawMember("node", 101, None),
        RawMember("way", 11, None),
        RawMember("relation", 1, None)
      )
    )

    val rawData = RawData(None, Seq(node1, node2, node3), Seq(way), Seq(relation))

    val log = Log.mock

    val data = new DataBuilder(rawData, log).data

    //log.messages.size should equal(0)

    data.relations(1).nodeMembers.map(_.node.id) should equal(Seq(101))
    data.relations(1).wayMembers.map(_.way.id) should equal(Seq(11))
    data.relations(1).relationMembers shouldBe empty

    log.messages.foreach(println)
    log.messages.size should equal(1)
    log.messages.head should equal("WARN data inconsistancy: relation 1 contains self reference, continue processing without this reference")
  }
}
