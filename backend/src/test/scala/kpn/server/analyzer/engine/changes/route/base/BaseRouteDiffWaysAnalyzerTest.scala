package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.data.MemberType
import kpn.api.common.diff.WayInfo
import kpn.api.custom.Tags
import kpn.api.time.Timestamps
import kpn.core.test.TestData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newWayUpdate
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.scalamock.stubs.Stubs

class BaseRouteDiffWaysAnalyzerTest extends UnitTest with Stubs {

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

    val wayDiffsInfo = new BaseRouteDiffWaysAnalyzer().analyze(before, after).get
    assertEqual(
      wayDiffsInfo.removed,
      Seq(
        WayInfo(
          id = 102,
          version = 0,
          changeSetId = 1,
          timestamp = Timestamps.default, // "2015-08-11T00:00:00Z",
          tags = Tags.from(
            "highway" -> "unclassified"
          )
        )
      )
    )
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

    val wayDiffsInfo = new BaseRouteDiffWaysAnalyzer().analyze(before, after).get
    assertEqual(
      wayDiffsInfo.added,
      Seq(
        WayInfo(
          id = 102,
          version = 0,
          changeSetId = 1,
          timestamp = Timestamps.default, // "2015-08-11T00:00:00Z",
          tags = Tags.from(
            "highway" -> "unclassified"
          )
        )
      )
    )
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

    val wayDiffsInfo = new BaseRouteDiffWaysAnalyzer().analyze(before, after).get
    assertEqual(
      wayDiffsInfo.updated,
      Seq(
        newWayUpdate(
          id = 101,
          before = newMetaData(),
          after = newMetaData(),
          directionReversed = true
        )
      )
    )
  }
}
