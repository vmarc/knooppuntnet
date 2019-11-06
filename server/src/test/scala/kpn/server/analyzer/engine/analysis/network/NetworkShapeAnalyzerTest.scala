package kpn.server.analyzer.engine.analysis.network

import kpn.core.test.TestData
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import kpn.shared.Bounds
import kpn.shared.data.Relation
import kpn.shared.data.raw.RawMember
import kpn.shared.network.NetworkShape
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NetworkShapeAnalyzerTest extends FunSuite with Matchers {

  ignore("shape") {

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

    val shape = analyze(networkRelation)

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,0.0],[20.0,0.0],[20.0,1.0],[10.0,1.0],[10.0,0.0]")
  }

  ignore("connection nodes are excluded from the shape") {

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

    val shape = analyze(networkRelation)

    shape.get.bounds should equal(Bounds(10.0, 0.0, 20.0, 1.0))
    shape.get.coordinates should equal("[10.0,1.0],[20.0,0.0],[20.0,1.0],[10.0,1.0]")
  }

  private def analyze(networkRelation: Relation): Option[NetworkShape] = {
    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    new NetworkShapeAnalyzer(relationAnalyzer, networkRelation).shape
  }

}
