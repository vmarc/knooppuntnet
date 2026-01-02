package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.common.Ref
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.changes.route.base.BaseRouteDiffAnalyzer

//  changeSetId = 104737699L
//  replicationNumber = 4542690L
class Issue183_DeletedNode1 extends UnitTest {

  private val deletedNodeId = 8731919671L
  private val replacementNodeId = 8734240777L

  test("rpn node removed in way in orphan rpn route") {

    val contextBefore = CaseStudy.analyze("12713351-before")
    val deletedNodeBefore = contextBefore.routeNodesAnalysis.startNode.get
    deletedNodeBefore.node.id should equal(deletedNodeId)
    deletedNodeBefore.name should equal("59")

    val contextAfter = CaseStudy.analyze("12713351-after")
    val deletedNodeAfter = contextAfter.routeNodesAnalysis.startNode.get
    deletedNodeAfter.node.id should equal(replacementNodeId)
    deletedNodeAfter.name should equal("59")

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(contextBefore, contextAfter)

    val addedNode = routeDiff.nodeDiffs.head.added.head
    val removedNode = routeDiff.nodeDiffs.head.removed.head
    addedNode should equal(Ref(replacementNodeId, "59"))
    removedNode should equal(Ref(deletedNodeId, "59"))
  }
}
