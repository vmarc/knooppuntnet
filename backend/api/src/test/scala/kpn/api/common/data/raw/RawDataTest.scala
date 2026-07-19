package kpn.api.common.data.raw

import kpn.core.test.TestData
import kpn.core.util.UnitTest

class RawDataTest extends UnitTest {

  test("nodes that appear multiple times should be included only once in the merged data") {

    val data1 = new TestData() {
      node(1001)
      node(1002)
    }.data.raw

    val data2 = new TestData() {
      node(1001)
      node(1003)
    }.data.raw

    val merged = RawData.merge(data1, data2)

    merged.nodes.map(_.id) should equal(Seq(1001L, 1002L, 1003L))
  }

  test("ways that appear multiple times should be included only once in the merged data") {

    val data1 = new TestData() {
      way(101)
      way(102)
    }.data.raw

    val data2 = new TestData() {
      way(101)
      way(103)
    }.data.raw

    val merged = RawData.merge(data1, data2)

    merged.ways.map(_.id) should equal(Seq(101L, 102L, 103L))
  }

  test("relations that appear multiple times should be included only once in the merged data") {

    val data1 = new TestData() {
      relation(1)
      relation(2)
    }.data.raw

    val data2 = new TestData() {
      relation(1)
      relation(3)
    }.data.raw

    val merged = RawData.merge(data1, data2)

    merged.relations.map(_.id) should equal(Seq(1L, 2L, 3L))
  }
}
