package kpn.api.common.location

import enumeratum._

sealed trait SurveyParameter extends EnumEntry

object SurveyParameter extends Enum[SurveyParameter] {

  val values: IndexedSeq[SurveyParameter] = findValues

  case object unknown extends SurveyParameter

  case object lastMonth extends SurveyParameter

  case object lastHalfYear extends SurveyParameter

  case object lastYear extends SurveyParameter

  case object lastTwoYears extends SurveyParameter

  case object older extends SurveyParameter
}
