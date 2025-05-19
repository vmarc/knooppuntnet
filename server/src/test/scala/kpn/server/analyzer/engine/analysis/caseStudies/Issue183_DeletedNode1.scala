package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.SharedTestObjects
import kpn.api.common.common.Ref
import kpn.api.common.diff.RouteData
import kpn.core.history.RouteDiffAnalyzer
import kpn.core.util.UnitTest
import org.scalamock.scalatest.MockFactory

//  changeSetId = 104737699L
//  replicationNumber = 4542690L
class Issue183_DeletedNode1 extends UnitTest with MockFactory with SharedTestObjects {

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

    val routeUpdate = new RouteDiffAnalyzer(RouteData.from(contextBefore), RouteData.from(contextAfter), None).analysis

    val addedNode = routeUpdate.diffs.nodeDiffs.head.added.head
    val removedNode = routeUpdate.diffs.nodeDiffs.head.removed.head
    addedNode should equal(Ref(replacementNodeId, "59"))
    removedNode should equal(Ref(deletedNodeId, "59"))
  }
}
