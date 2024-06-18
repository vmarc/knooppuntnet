package kpn.server.analyzer.engine.analysis.common

import kpn.api.common.data.Tagable
import kpn.api.custom.Day

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.util.matching.Regex

object SurveyDateAnalyzer {

  private val monthPattern: Regex = """(20\d\d)-(0[1-9]|1[0-2])""".r
  private val dayPattern: Regex = """(20\d\d)-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])""".r

  def analyze(tagable: Tagable): Try[Option[Day]] = {
    new SurveyDateAnalyzer(tagable).analyze()
  }
}

class SurveyDateAnalyzer(tagable: Tagable) {

  def analyze(): Try[Option[Day]] = {
    surveyDate match {
      case None => Success(None)
      case Some(string) =>
        string match {
          case SurveyDateAnalyzer.monthPattern(year, month) =>
            Success(
              Some(
                Day(
                  year.toInt,
                  month.toInt
                )
              )
            )
          case SurveyDateAnalyzer.dayPattern(year, month, day) =>
            Success(
              Some(
                Day(
                  year.toInt,
                  month.toInt,
                  Some(day.toInt)
                )
              )
            )
          case _ => Failure(null)
        }
    }
  }

  private def surveyDate: Option[String] = {
    tagable.tagValue("survey:date") match {
      case Some(string) => Some(string)
      case None =>
        if (tagable.hasTag("source", "survey")) {
          tagable.tagValue("source:date")
        }
        else {
          None
        }
    }
  }
}
