package kpn.server.analyzer.engine.analysis.common

import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

import scala.util.Failure
import scala.util.Success

class SurveyDateAnalyzerTest extends UnitTest {

  test("no survey date") {
    SurveyDateAnalyzer.analyze(Tags.empty) should equal(Success(None))
  }

  test("invalid syntax") {
    SurveyDateAnalyzer.analyze(
      Tags.from(
        "survey:date" -> "bla"
      )
    ) should equal(
      Failure(null)
    )
  }

  test("survey:date YYYY-MM-DD") {
    SurveyDateAnalyzer.analyze(
      Tags.from(
        "survey:date" -> "2020-08-11"
      )
    ) should equal(
      Success(
        Some(
          Day(2020, 8, 11)
        )
      )
    )
  }

  test("survey:date YYYY-MM") {
    SurveyDateAnalyzer.analyze(
      Tags.from(
        "survey:date" -> "2020-08"
      )
    ) should equal(
      Success(
        Some(
          Day(2020, 8)
        )
      )
    )
  }
  test("source=survey + source:date") {
    SurveyDateAnalyzer.analyze(
      Tags.from(
        "source" -> "survey",
        "source:date" -> "2020-08"
      )
    ) should equal(
      Success(
        Some(
          Day(2020, 8)
        )
      )
    )
  }
}
