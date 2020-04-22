package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.Node
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class GeometryDigestAnalyzerTest extends UnitTest with SharedTestObjects {

  test("calculate digest") {

    val context = testContext(newNode1, newNode2a)

    GeometryDigestAnalyzer.analyze(context).geometryDigest should equal(
      Some("35f0bbdd870ed7fbe390b69b7dbd24b6b4b5a89e")
    )

    GeometryDigestAnalyzer.analyze(context).geometryDigest should equal(
      Some("35f0bbdd870ed7fbe390b69b7dbd24b6b4b5a89e")
    )
  }

  test("calculate another digest") {

    val context = testContext(newNode1, newNode2b)

    GeometryDigestAnalyzer.analyze(context).geometryDigest should equal(
      Some("0fe7cb4490c063ba839199aa955f645ce32e8650")
    )
  }

  private def testContext(node1: Node, node2: Node): RouteAnalysisContext = {
    RouteAnalysisContext(
      null,
      null,
      null,
      orphan = false,
      allWayNodes = Some(
        Seq(
          node1,
          node2
        )
      )
    )
  }

  private def newNode1 = newNode(1001, "11.0", "21.0")

  private def newNode2a = newNode(1002, "12.0", "22.0")

  private def newNode2b = newNode(1002, "12.0", "22.1")

}
