package kpn.server.analyzer.engine.changes.route.base

import kpn.core.test.TestObjects.newRelation
import kpn.core.test.TestObjects.newRouteMemberInfo
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.scalamock.stubs.Stubs

class BaseRouteDiffMemberAnalyzerTest extends UnitTest with Stubs {

  test("member order unchanged") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      )
    )

    val memberOrderChanged = new BaseRouteDiffMemberAnalyzer().memberOrderChanged(before, after)
    memberOrderChanged should equal(false)
  }

  test("member order changed") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
          newRouteMemberInfo(101),
        )
      )
    )

    val memberOrderChanged = new BaseRouteDiffMemberAnalyzer().memberOrderChanged(before, after)
    memberOrderChanged should equal(true)
  }

  test("member order changed false when member count is different") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
        )
      )
    )

    val memberOrderChanged = new BaseRouteDiffMemberAnalyzer().memberOrderChanged(before, after)
    memberOrderChanged should equal(false)
  }

  test("member order changed false when member count is same but ids different") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(103),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeMembers = Some(
        Seq(
          newRouteMemberInfo(101),
          newRouteMemberInfo(102),
          newRouteMemberInfo(104),
        )
      )
    )

    val memberOrderChanged = new BaseRouteDiffMemberAnalyzer().memberOrderChanged(before, after)
    memberOrderChanged should equal(false)
  }
}
