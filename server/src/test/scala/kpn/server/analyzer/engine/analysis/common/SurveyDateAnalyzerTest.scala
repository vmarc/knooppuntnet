package kpn.server.analyzer.engine.analysis.common

import kpn.api.custom.Day
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newNode
import kpn.core.util.UnitTest

import scala.util.Failure
import scala.util.Success
import scala.util.Try

class SurveyDateAnalyzerTest extends UnitTest {

  test("no survey date") {
    analyze() should equal(Success(None))
  }

  test("invalid syntax") {
    analyze(
      "survey:date" -> "bla"
    ) should equal(
      Failure(null)
    )
  }

  test("survey:date YYYY-MM-DD") {
    analyze(
      "survey:date" -> "2020-08-11"
    ) should equal(
      Success(
        Some(
          Day(2020, 8, 11)
        )
      )
    )
  }

  test("survey:date YYYY-MM") {
    analyze(
      "survey:date" -> "2020-08"
    ) should equal(
      Success(
        Some(
          Day(2020, 8)
        )
      )
    )
  }

  test("source=survey + source:date") {
    analyze(
      "source" -> "survey",
      "source:date" -> "2020-08"
    ) should equal(
      Success(
        Some(
          Day(2020, 8)
        )
      )
    )
  }

  private def analyze(tags: (String, String)*): Try[Option[Day]] = {
    SurveyDateAnalyzer.analyze(
      newNode(tags = Tags.from(tags: _*))
    )
  }
}
