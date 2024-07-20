package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class Issue45_OneWayRoute extends UnitTest {

  test("route 60-61") {
    val context = CaseStudy.analyze("7328339")
    context.oldFacts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    if (Redesign.enableNewFactTests) {
      context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    }
  }

  test("route 63-64") {
    val context = CaseStudy.analyze("9515132")
    context.oldFacts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    if (Redesign.enableNewFactTests) {
      context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    }
  }

  test("route 84-86") {
    val context = CaseStudy.analyze("6635664")
    context.oldFacts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    if (Redesign.enableNewFactTests) {
      context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    }
  }

  test("route 74-86") {
    val context = CaseStudy.analyze("6635670")
    context.oldFacts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    if (Redesign.enableNewFactTests) {
      context.facts should equal(Seq(Fact.RouteNameDeprecatedNoteTag, Fact.RouteOneWay))
    }
  }
}
