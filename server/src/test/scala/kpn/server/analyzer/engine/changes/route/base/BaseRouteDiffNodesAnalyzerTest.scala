package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.common.Ref
import kpn.api.common.diff.route.RouteNodeDiff
import kpn.core.test.TestObjects.newNode
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis
import org.scalamock.stubs.Stubs

class BaseRouteDiffNodesAnalyzerTest extends UnitTest with Stubs {

  test("start node diff") {

    val before = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(
            RouteNodeAnalysis(
              node = newNode(),
              name = "01",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startNode = Some(
            RouteNodeAnalysis(
              node = newNode(1002),
              name = "02",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val nodeDiffs = new BaseRouteDiffNodesAnalyzer().analyze(before, after)

    assertEqual(
      nodeDiffs,
      Seq(
        RouteNodeDiff(
          title = "startNode",
          added = Seq(
            Ref(1002, "02")
          ),
          removed = Seq(
            Ref(1001, "01")
          )
        )
      )
    )
  }

  test("end node diff") {

    val before = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          endNode = Some(
            RouteNodeAnalysis(
              node = newNode(),
              name = "01",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          endNode = Some(
            RouteNodeAnalysis(
              node = newNode(1002),
              name = "02",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val nodeDiffs = new BaseRouteDiffNodesAnalyzer().analyze(before, after)

    assertEqual(
      nodeDiffs,
      Seq(
        RouteNodeDiff(
          title = "endNode",
          added = Seq(
            Ref(1002, "02")
          ),
          removed = Seq(
            Ref(1001, "01")
          )
        )
      )
    )
  }

  test("start tentacle node diff") {

    val before = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startTentacleNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(),
              name = "01",
              alternateName = "",
              isInWay = false
            )
          ),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          startTentacleNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(1002),
              name = "02",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val nodeDiffs = new BaseRouteDiffNodesAnalyzer().analyze(before, after)

    assertEqual(
      nodeDiffs,
      Seq(
        RouteNodeDiff(
          title = "startTentacleNodes",
          added = Seq(
            Ref(1002, "02")
          ),
          removed = Seq(
            Ref(1001, "01")
          )
        )
      )
    )
  }

  test("end tentacle node diff") {

    val before = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          endTentacleNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(),
              name = "01",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          endTentacleNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(1002),
              name = "02",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val nodeDiffs = new BaseRouteDiffNodesAnalyzer().analyze(before, after)

    assertEqual(
      nodeDiffs,
      Seq(
        RouteNodeDiff(
          title = "endTentacleNodes",
          added = Seq(
            Ref(1002, "02")
          ),
          removed = Seq(
            Ref(1001, "01")
          )
        )
      )
    )
  }

  test("redundant node diff") {

    val before = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          redundantNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(),
              name = "01",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      relation = newRelation(),
      subRelationTree = None,
      _routeNodesAnalysis = Some(
        RouteNodesAnalysis(
          redundantNodes = Seq(
            RouteNodeAnalysis(
              node = newNode(1002),
              name = "02",
              alternateName = "",
              isInWay = false
            )
          )
        )
      )
    )

    val nodeDiffs = new BaseRouteDiffNodesAnalyzer().analyze(before, after)

    assertEqual(
      nodeDiffs,
      Seq(
        RouteNodeDiff(
          title = "redundantNodes",
          added = Seq(
            Ref(1002, "02")
          ),
          removed = Seq(
            Ref(1001, "01")
          )
        )
      )
    )
  }
}
