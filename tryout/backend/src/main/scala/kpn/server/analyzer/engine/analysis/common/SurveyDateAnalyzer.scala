package kpn.server.analyzer.engine.analysis.common

import kpn.api.common.data.Tagable
import kpn.api.custom.Day
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer.DayPattern
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer.MonthPattern

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.matching.Regex

object SurveyDateAnalyzer {

  private val MonthPattern: Regex = """(20\d\d)-(0[1-9]|1[0-2])""".r
  private val DayPattern: Regex = """(20\d\d)-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])""".r

  def analyze(tagable: Tagable): Try[Option[Day]] = {
    new SurveyDateAnalyzer(tagable).analyze()
  }
}

class SurveyDateAnalyzer(tagable: Tagable) {

  def analyze(): Try[Option[Day]] = {
    surveyDate.map(parse).getOrElse(Success(None))
  }

  private def parse(string: String): Try[Option[Day]] = {
    string match {
      case MonthPattern(year, month) => createDay(year.toInt, month.toInt)
      case DayPattern(year, month, day) => createDay(year.toInt, month.toInt, Some(day.toInt))
      case _ => Failure(null)
    }
  }

  private def surveyDate: Option[String] = {
    tagable.tagValue("survey:date") orElse {
      if (tagable.hasTag("source", "survey")) {
        tagable.tagValue("source:date")
      }
      else {
        None
      }
    }
  }

  private def createDay(year: Int, month: Int, day: Option[Int] = None): Try[Option[Day]] = {
    Success(Some(Day(year, month, day)))
  }
}
