package kpn.server.analyzer.engine.changes.data

import kpn.api.common.SharedTestObjects
import kpn.core.data.Data
import kpn.core.test.TestData2
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class AnalysisDataTest extends FunSuite with Matchers with SharedTestObjects {

  test("isReferencedRoute") {

    val data = buildData()

    val relationAnalyzer = {
      val analysisContext = new AnalysisContext()
      new RelationAnalyzerImpl(analysisContext)
    }

    val analysisData = AnalysisData()
    analysisData.networks.watched.add(1, relationAnalyzer.toElementIds(data.relations(1)))
    analysisData.networks.watched.add(2, relationAnalyzer.toElementIds(data.relations(2)))

    analysisData.networks.isReferencingRelation(11) should equal(true)
    analysisData.networks.isReferencingRelation(12) should equal(true)
    analysisData.networks.isReferencingRelation(13) should equal(false)
  }

  private def buildData(): Data = {
    TestData2()
      .networkNode(1001, "01") // referenced in network1 and network2 and orphan route
      .networkNode(1002, "02") // referenced in network1
      .networkNode(1003, "03") // referenced in network2
      .networkNode(1004, "04") // referenced in orphan route
      .way(101, 1001, 1002) // network 1
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(1, "network1", Seq(newMember("relation", 11)))
      .way(102, 1001, 1003) // network 2
      .route(12, "01-03", Seq(newMember("way", 102)))
      .networkRelation(2, "network2", Seq(newMember("relation", 12)))
      .way(103, 1001, 1003) // orphan route
      .route(13, "01-04", Seq(newMember("way", 103)))
      .data
  }
}
