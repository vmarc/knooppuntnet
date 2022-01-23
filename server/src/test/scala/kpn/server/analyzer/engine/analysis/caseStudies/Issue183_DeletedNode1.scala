package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory

class Issue183_DeletedNode1 extends UnitTest with MockFactory with SharedTestObjects {

  private val changeSetId = 104737699L
  private val replicationNumber = 4542690L
  private val deletedNodeId = 8731919671L
  private val replacementNodeId = 8734240777L

  test("rpn node removed in way in orphan rpn route") {

    val analysisBefore = CaseStudy.routeAnalysis("12713351-before")
    val deletedNodeBefore = analysisBefore.route.analysis.map.endNodes.head
    deletedNodeBefore.id should equal(deletedNodeId)
    deletedNodeBefore.name should equal("59")

    val analysisAfter = CaseStudy.routeAnalysis("12713351-after")
    val deletedNodeAfter = analysisAfter.route.analysis.map.endNodes.head
    deletedNodeAfter.id should equal(replacementNodeId)
    deletedNodeAfter.name should equal("59")

    val routeUpdate = new RouteDiffAnalyzer(analysisBefore, analysisAfter).analysis

    val addedNode = routeUpdate.diffs.nodeDiffs.head.added.head
    val removedNode = routeUpdate.diffs.nodeDiffs.head.removed.head
    addedNode should equal(Ref(replacementNodeId, "59"))
    removedNode should equal(Ref(deletedNodeId, "59"))
  }

}
