package kpn.api.common.data.raw

import kpn.api.ApiTestObjects.newRawNode
import kpn.api.ApiTestObjects.newRawRelation
import kpn.api.ApiTestObjects.newRawWay
import kpn.core.util.UnitTest

class RawDataTest extends UnitTest {

  test("nodes that appear multiple times should be included only once in the merged data") {

    val data1 = RawData(
      nodes = Seq(
        newRawNode(1001),
        newRawNode(1002),
      )
    )

    val data2 = RawData(
      nodes = Seq(
        newRawNode(1001),
        newRawNode(1003),
      )
    )

    val merged = RawData.merge(data1, data2)

    merged.nodes.map(_.id) should equal(Seq(1001L, 1002L, 1003L))
  }

  test("ways that appear multiple times should be included only once in the merged data") {

    val data1 = RawData(
      ways = Seq(
        newRawWay(101),
        newRawWay(102),
      )
    )

    val data2 = RawData(
      ways = Seq(
        newRawWay(101),
        newRawWay(103),
      )
    )

    val merged = RawData.merge(data1, data2)

    merged.ways.map(_.id) should equal(Seq(101L, 102L, 103L))
  }

  test("relations that appear multiple times should be included only once in the merged data") {

    val data1 = RawData(
      relations = Seq(
        newRawRelation(1),
        newRawRelation(2),
      )
    )

    val data2 = RawData(
      relations = Seq(
        newRawRelation(1),
        newRawRelation(3),
      )
    )

    val merged = RawData.merge(data1, data2)

    merged.relations.map(_.id) should equal(Seq(1L, 2L, 3L))
  }
}
