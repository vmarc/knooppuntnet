package kpn.server.analyzer.engine.monitor

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Relation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class MonitorRouteStructureAnalyzerTest extends UnitTest with SharedTestObjects {

  ignore("simulation") {
    val relation = CaseStudy.load("/case-studies/monitor/4840541.xml")
    println(s"relation.members.size=${relation.members.size}")
    new MonitorRouteStructureAnalyzer().analyzeRoute(relation)
  }

  test("continous") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6, 7)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(1, 2, 3, 4, 5, 6, 7)
      )
    )
  }

  test("continous - first way reversed") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "", 3, 2, 1)
      memberWay(11, "", 3, 4, 5)
      memberWay(12, "", 5, 6, 7)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(1, 2, 3, 4, 5, 6, 7)
      )
    )
  }

  test("continous - second and third way reversed") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "", 1, 2, 3)
      memberWay(11, "", 5, 4, 3)
      memberWay(12, "", 7, 6, 5)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(1, 2, 3, 4, 5, 6, 7)
      )
    )
  }

  test("2 gaps - 3 segments") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "", 1, 2)
      memberWay(11, "", 3, 4)
      memberWay(12, "", 4, 5)
      memberWay(13, "", 6, 7)
      memberWay(14, "", 7, 8)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(1, 2),
        Seq(3, 4, 5),
        Seq(6, 7, 8),
      )
    )
  }

  test("start way has forward role") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "forward", 1, 2)
      memberWay(11, "", 3, 2)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(1, 2, 3),
      )
    )
  }

  test("start way has forward role, no connection to second way") {
    val relation = new MonitorRouteTestData() {
      memberWay(10, "forward", 2, 1)
      memberWay(11, "", 3, 2)
    }.relation

    analyze(relation).shouldMatchTo(
      Seq(
        Seq(2, 1),
        Seq(3, 2),
      )
    )
  }


  //  test("forward") {
  //    val relation = new MonitorRouteTestData() {
  //      memberWay(10, "", 1, 2)
  //      memberWay(11, "forward", 2, 3)
  //      memberWay(12, "forward", 3, 8)
  //      memberWay(13, "forward", 7, 2)
  //      memberWay(14, "forward", 8, 7)
  //      memberWay(15, "", 8, 9)
  //    }.relation
  //
  //    analyze(relation).shouldMatchTo(
  //      Seq(
  //        // forward path, backward path
  //        Seq(1, 2, 3, 8, 9),
  //      )
  //    )
  //  }


  private def analyze(relation: Relation): Seq[Seq[Long]] = {
    val segments = new MonitorRouteStructureAnalyzer().analyzeRoute(relation)
    segments.map { segment =>
      segment.zipWithIndex.flatMap { case (routeWay, index) =>
        if (index == 0) {
          routeWay.nodeIds
        }
        else {
          routeWay.nodeIds.tail
        }
      }
    }
  }
}
