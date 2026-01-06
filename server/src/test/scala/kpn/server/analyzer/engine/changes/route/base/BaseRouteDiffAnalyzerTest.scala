package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteNameDiff
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.test.TestObjects.newRouteMemberInfo
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteNodesAnalysis
import org.scalamock.stubs.Stubs

class BaseRouteDiffAnalyzerTest extends UnitTest with Stubs {

  test("name diff") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(
          name = Some("01-02"),
        )
      ),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty)
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(
          name = Some("02-01"),
        )
      ),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty)
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.nameDiff should equal(
      Some(
        RouteNameDiff(Some("01-02"), Some("02-01"))
      )
    )
  }

  test("fact diff") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty),
      facts = Seq(
        Fact.RouteUnexpectedNode,
        Fact.RouteInaccessible
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty),
      facts = Seq(
        Fact.RouteUnexpectedRelation,
        Fact.RouteBroken,
        Fact.RouteInaccessible
      )
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.factDiffs should equal(
      Some(
        FactDiffs(
          resolved = Seq(
            Fact.RouteUnexpectedNode
          ),
          introduced = Seq(
            Fact.RouteUnexpectedRelation,
            Fact.RouteBroken
          ),
          remaining = Seq(
            Fact.RouteInaccessible
          )
        )
      )
    )
  }

  test("member order unchanged") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      ),
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      ),
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.memberOrderChanged should equal(false)
  }

  test("member order changed") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      ),
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
          newRouteMemberInfo(101),
        )
      ),
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.memberOrderChanged should equal(true)
  }

  test("member order changed false when member count is different") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      ),
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
        )
      ),
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.memberOrderChanged should equal(false)
  }

  test("member order changed false when member count is same but ids different") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      ),
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(104),
        )
      ),
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    routeDiff.memberOrderChanged should equal(false)
  }

  test("tags diff") {

    val before = BaseRouteAnalysisContext(
      newRelation(
        11,
        tags = Tags.from(
          "ref" -> "01-02",
          "network" -> "rwn",
          "type" -> "route",
          "route" -> "foot",
          "network:type" -> "node_network",
          "a" -> "1",
          "b" -> "1"
        )
      ),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty),
    )

    val after = BaseRouteAnalysisContext(
      newRelation(
        11,
        tags = Tags.from(
          "ref" -> "01-02",
          "network" -> "rwn",
          "type" -> "route",
          "route" -> "foot",
          "network:type" -> "node_network",
          "a" -> "2",
          "c" -> "2"
        )
      ),
      None,
      _routeNameAnalysis = Some(RouteNameAnalysis()),
      _routeNodesAnalysis = Some(RouteNodesAnalysis()),
      _routeMembers = Some(Seq.empty),
    )

    val routeDiff = new BaseRouteDiffAnalyzer().analyze(before, after)
    assertEqual(
      routeDiff.tagDiffs.get,
      TagDiffs(
        Seq(
          TagDiff.same("ref", "01-02"),
          TagDiff.same("network", "rwn"),
          TagDiff.same("type", "route"),
          TagDiff.same("route", "foot"),
          TagDiff.same("network:type", "node_network")
        ),
        Seq(
          TagDiff.add("c", "2"),
          TagDiff.delete("b", "1"),
          TagDiff.update("a", "1", "2"),
        )
      )
    )
  }
}
