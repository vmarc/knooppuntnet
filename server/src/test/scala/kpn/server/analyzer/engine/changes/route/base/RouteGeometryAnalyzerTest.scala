package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.route.GeometryDiff
import kpn.api.common.route.WayGeometry
import kpn.api.common.route.WayLine
import kpn.core.test.TestData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newWayGeometryUpdate
import kpn.core.util.UnitTest

class RouteGeometryAnalyzerTest extends UnitTest {

  private val analyzer = new RouteGeometryAnalyzer()

  test("geometry diff for newly created route") {

    // setup
    val before = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

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
    }

    val relation = before.data.relations(11)

    // execute
    val geometryDiff = analyzer.initialAnalyze(relation)

    // verify
    geometryDiff.get should equal(
      GeometryDiff(
        common = Seq.empty,
        update = Seq(
          newWayGeometryUpdate(
            wayId = 101,
            added = Some(
              Seq(
                WayLine(2, 15, "[[1.0001,1.0001],[1000,1000]]")
              )
            )
          ),
          newWayGeometryUpdate(
            wayId = 102,
            added = Some(
              Seq(
                WayLine(2, 15, "[[1.0002,1.0002],[1000,1000]]")
              )
            )
          )
        ),
        bounds = Bounds(1.0001, 1.0001, 1.0003, 1.0003)
      )
    )
  }

  test("no difference") {

    // setup
    val before = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

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
    }

    val relation = before.data.relations(11)

    // execute
    val result = analyzer.analyze(relation, relation)

    // verify
    result should equal(None)
  }

  test("add way") {
    // setup
    val before = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

      way(101, 1001, 1002)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }

    val after = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

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
    }

    val relationBefore = before.data.relations(11)
    val relationAfter = after.data.relations(11)

    // execute
    val geometryDiff = analyzer.analyze(relationBefore, relationAfter).get

    // verify
    geometryDiff should equal(
      GeometryDiff(
        common = Seq(
          WayGeometry(
            101, WayLine(2, 15, "[[1.0001,1.0001],[1000,1000]]")
          ),

        ),
        update = Seq(
          newWayGeometryUpdate(
            wayId = 102,
            added = Some(
              Seq(
                WayLine(2, 15, "[[1.0002,1.0002],[1000,1000]]")
              )
            )
          )
        ),
        bounds = Bounds(1.0002, 1.0002, 1.0003, 1.0003)
      )
    )
  }

  test("remove way") {

    // setup
    val before = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

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
    }

    val after = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")
      node(1003, latitude = "1.0003", longitude = "1.0003")

      way(101, 1001, 1002)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }

    val relationBefore = before.data.relations(11)
    val relationAfter = after.data.relations(11)

    // execute
    val geometryDiff = analyzer.analyze(relationBefore, relationAfter).get

    // verify
    geometryDiff should equal(
      GeometryDiff(
        common = Seq(
          WayGeometry(
            101, WayLine(2, 15, "[[1.0001,1.0001],[1000,1000]]")
          )
        ),
        update = Seq(
          newWayGeometryUpdate(
            wayId = 102,
            removed = Some(
              Seq(
                WayLine(2, 15, "[[1.0002,1.0002],[1000,1000]]")
              )
            )
          )
        ),
        bounds = Bounds(1.0002, 1.0002, 1.0003, 1.0003)
      )
    )
  }

  test("update way coordinates") {

    // setup
    val before = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0002", longitude = "1.0002")

      way(101, 1001, 1002)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }

    val after = new TestData() {
      node(1001, latitude = "1.0001", longitude = "1.0001")
      node(1002, latitude = "1.0003", longitude = "1.0003")

      way(101, 1001, 1002)

      route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
    }

    val relationBefore = before.data.relations(11)
    val relationAfter = after.data.relations(11)

    // execute
    val geometryDiff = analyzer.analyze(relationBefore, relationAfter).get

    // verify
    geometryDiff should equal(
      GeometryDiff(
        common = Seq.empty,
        update = Seq(
          newWayGeometryUpdate(
            wayId = 101,
            removed = Some(
              Seq(
                WayLine(2, 16, "[[1.0001,1.0001],[1000,1000]]")
              )
            ),
            added = Some(
              Seq(
                WayLine(2, 31, "[[1.0001,1.0001],[2000,2000]]")
              )
            )
          )
        ),
        bounds = Bounds(1.0001, 1.0001, 1.0003, 1.0003)
      )
    )
  }
}
