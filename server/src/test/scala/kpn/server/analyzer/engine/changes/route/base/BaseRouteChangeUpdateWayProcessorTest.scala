package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.data.MemberType
import kpn.core.test.TestData
import kpn.core.test.TestObjects.newMember
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.scalamock.stubs.Stubs

class BaseRouteChangeUpdateWayProcessorTest extends UnitTest with Stubs {

  test("removed way") {

    val beforeData = new TestData() {
      node(1001)
      node(1002)
      node(1003)

      way(101, 1001, 1002)
      way(102, 1002, 1003)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101),
          newMember(MemberType.Way, 102)
        )
      )
    }.data

    val afterData = new TestData() {
      node(1001)
      node(1002)
      node(1003)

      way(101, 1001, 1002)
      way(102, 1002, 1003)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }.data

    val before = BaseRouteAnalysisContext(
      beforeData.relations(11),
      None
    )

    val after = BaseRouteAnalysisContext(
      afterData.relations(11),
      None
    )

    val wayDiffsInfo = new BaseRouteChangeUpdateWayProcessor().process(before, after).get
    wayDiffsInfo.removed.map(_.id) should equal(Seq(102)) // TODO compare complete WayInfo objects
  }

  test("added way") {

    val beforeData = new TestData() {
      node(1001)
      node(1002)
      node(1003)

      way(101, 1001, 1002)
      way(102, 1002, 1003)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }.data

    val afterData = new TestData() {
      node(1001)
      node(1002)
      node(1003)

      way(101, 1001, 1002)
      way(102, 1002, 1003)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101),
          newMember(MemberType.Way, 102)
        )
      )
    }.data

    val before = BaseRouteAnalysisContext(
      beforeData.relations(11),
      None
    )

    val after = BaseRouteAnalysisContext(
      afterData.relations(11),
      None
    )

    val wayDiffsInfo = new BaseRouteChangeUpdateWayProcessor().process(before, after).get
    wayDiffsInfo.added.map(_.id) should equal(Seq(102)) // TODO compare complete WayInfo objects
  }

  test("updated way") {

    val beforeData = new TestData() {
      node(1001)
      node(1002)

      way(101, 1001, 1002)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }.data

    val afterData = new TestData() {
      node(1001)
      node(1002)

      way(101, 1002, 1001) // direction reversed

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }.data

    val before = BaseRouteAnalysisContext(
      beforeData.relations(11),
      None
    )

    val after = BaseRouteAnalysisContext(
      afterData.relations(11),
      None
    )

    val wayDiffsInfo = new BaseRouteChangeUpdateWayProcessor().process(before, after).get
    wayDiffsInfo.updated.map(_.id) should equal(Seq(101)) // TODO compare complete WayUpdate objects
  }
}
