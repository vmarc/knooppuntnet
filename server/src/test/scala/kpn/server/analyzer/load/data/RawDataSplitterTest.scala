package kpn.server.analyzer.load.data

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.raw.RawData
import kpn.api.common.data.raw.RawMember
import kpn.core.test.TestData
import kpn.core.util.MockLog
import kpn.core.util.UnitTest

class RawDataSplitterTest extends UnitTest with SharedTestObjects {

  test("extract regular network relation") {

    val d = new TestData() {
      networkNode(10001, "01")
      networkNode(10002, "02")
      networkNode(10003, "03")
      networkNode(10004, "04")
      networkNode(10005, "05") // orphan node (not part of any route or network)
      node(10008) // referenced in way
      node(10009) // not referenced

      way(1001, 10001, 10008, 10002)
      way(1002, 10003, 10004) // not referenced

      route(101, "01-02",
        Seq(
          newMember("node", 10001),
          newMember("way", 1001),
          newMember("node", 10002)
        )
      )
      route(102, "03-04",
        Seq(
          newMember("node", 10003),
          newMember("node", 10004)
        )
      )
      route(103, "88-99", Seq()) // not referenced

      networkRelation(1, "name",
        Seq(
          newMember("node", 10001),
          newMember("node", 10002),
          newMember("relation", 101),
          newMember("relation", 102)
        )
      )
      networkRelation(2, "name", Seq())
      networkRelation(3, "name", Seq())
    }.data.raw

    {
      val log = new MockLog()
      val rawData = new RawDataSplitter(log).extractRelation(d, 1)
      log.messages should be(empty)

      rawData.nodes.map(_.id).sorted should equal(Seq(10001L, 10002L, 10003L, 10004L, 10008L))
      rawData.ways.map(_.id).sorted should equal(Seq(1001L))
      rawData.relations.map(_.id).sorted should equal(Seq(1L, 101L, 102L))
    }

    {
      val log = new MockLog()
      val rawData = new RawDataSplitter(log).extractRelation(d, 2)
      log.messages should be(empty)

      rawData.nodes.map(_.id) shouldBe empty
      rawData.ways.map(_.id) shouldBe empty
      rawData.relations.map(_.id) should equal(Seq(2))
    }
  }

  test("extract relation with direct circular reference to itself") {

    val relation = newRawRelation(101, members = Seq(RawMember("relation", 101, None)))
    val rawData = RawData(relations = Seq(relation))

    assertRelationsInExtract(rawData, 101, Seq(101L))
  }

  test("extract relation with indirect circular reference") {

    val relation1 = newRawRelation(101, members = Seq(RawMember("relation", 102, None)))
    val relation2 = newRawRelation(102, members = Seq(RawMember("relation", 101, None)))
    val relation3 = newRawRelation(103)

    val rawData = RawData(relations = Seq(relation1, relation2, relation3))

    assertRelationsInExtract(rawData, 101, Seq(101L, 102L))
    assertRelationsInExtract(rawData, 102, Seq(101L, 102L))
    assertRelationsInExtract(rawData, 103, Seq(103L))
  }

  test("inconsistant data: node referenced in relation missing") {

    val relation = newRawRelation(1, members = Seq(RawMember("node", 1001, None)))
    val rawData = RawData(relations = Seq(relation))

    assertInconsistant(rawData, 1, "ERROR Inconsistant data: missing nodes in relation 1: 1001")
  }

  test("inconsistant data: way referenced in relation missing") {

    val relation = newRawRelation(1, members = Seq(RawMember("way", 101, None)))
    val rawData = RawData(relations = Seq(relation))

    assertInconsistant(rawData, 1, "ERROR Inconsistant data: missing ways in relation 1: 101")
  }

  test("inconsistant data: relation referenced in relation missing") {

    val relation = newRawRelation(1, members = Seq(RawMember("relation", 101, None)))
    val rawData = RawData(relations = Seq(relation))

    assertInconsistant(rawData, 1, "ERROR Inconsistant data: missing referred relation in relation 1: 101")
  }

  test("inconsistant data: node referenced in way missing") {

    val way = newRawWay(101, nodeIds = Seq(1001))
    val relation = newRawRelation(1, members = Seq(RawMember("way", 101, None)))
    val rawData = RawData(ways = Seq(way), relations = Seq(relation))

    assertInconsistant(rawData, 1, "ERROR Inconsistant data: missing nodes in ways: 1001")
  }

  private def assertRelationsInExtract(rawData: RawData, extractedRelationId: Long, expectedIds: Seq[Long]): Unit = {
    val log = new MockLog()
    new RawDataSplitter(log).extractRelation(rawData, extractedRelationId).relations.map(_.id).sorted should equal(expectedIds)
    log.messages should be(empty)
  }

  private def assertInconsistant(rawData: RawData, id: Long, expectedError: String): Unit = {
    val log = new MockLog()
    new RawDataSplitter(log).extractRelation(rawData, id)
    log.messages should equal(Seq(expectedError))
  }
}
