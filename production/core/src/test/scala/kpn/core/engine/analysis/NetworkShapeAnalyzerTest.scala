package kpn.core.engine.analysis

import kpn.core.test.TestData
import kpn.shared.Bounds
import kpn.shared.data.raw.RawMember
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkShapeAnalyzerTest extends FunSuite with Matchers {

  test("shape") {

    val networkRelation = new TestData() {
      node(1001, latitude = "10", longitude = "1")
      node(1002, latitude = "20", longitude = "1")
      node(1003, latitude = "20", longitude = "0")
      node(1004, latitude = "10", longitude = "0")

      networkRelation(
        1,
        "",
        members = Seq(
          RawMember("node", 1001, None),
          RawMember("node", 1002, None),
          RawMember("node", 1003, None),
          RawMember("node", 1004, None)
        )
      )
    }.data.relations(1)

    val shape = new NetworkShapeAnalyzer(networkRelation).shape

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,0.0],[20.0,0.0],[20.0,1.0],[10.0,1.0],[10.0,0.0]")
  }

  test("connection nodes are excluded from the shape") {

    val networkRelation = new TestData() {
      node(1001, latitude = "10", longitude = "1")
      node(1002, latitude = "20", longitude = "1")
      node(1003, latitude = "20", longitude = "0")
      node(1004, latitude = "10", longitude = "0")

      networkRelation(
        1,
        "",
        members = Seq(
          RawMember("node", 1001, None),
          RawMember("node", 1002, None),
          RawMember("node", 1003, None),
          RawMember("node", 1004, Some("connection"))
        )
      )
    }.data.relations(1)

    val shape = new NetworkShapeAnalyzer(networkRelation).shape

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,1.0],[20.0,0.0],[20.0,1.0],[10.0,1.0]")
  }
}
