package kpn.server.analyzer.engine.analysis.network

import kpn.api.common.Bounds
import kpn.api.common.data.raw.RawMember
import kpn.api.common.network.NetworkShape
import kpn.api.custom.Relation
import kpn.core.test.TestData
import kpn.core.util.UnitTest

class NetworkShapeAnalyzerTest extends UnitTest {

  ignore("shape") {

    val relation = new TestData() {
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

    val shape = analyze(relation)

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,0.0],[20.0,0.0],[20.0,1.0],[10.0,1.0],[10.0,0.0]")
  }

  ignore("connection nodes are excluded from the shape") {

    val relation = new TestData() {
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

    val shape = analyze(relation)

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,1.0],[20.0,0.0],[20.0,1.0],[10.0,1.0]")
  }

  private def analyze(networkRelation: Relation): Option[NetworkShape] = {
    new NetworkShapeAnalyzer(networkRelation).shape
  }

}
