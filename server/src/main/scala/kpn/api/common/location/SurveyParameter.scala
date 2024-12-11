package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait SurveyParameter extends EnumEntry

object SurveyParameter extends Enum[SurveyParameter] {

  val values: IndexedSeq[SurveyParameter] = findValues

  final case object unknown extends SurveyParameter

  final case object lastMonth extends SurveyParameter

  final case object lastHalfYear extends SurveyParameter

  final case object lastYear extends SurveyParameter

  final case object lastTwoYears extends SurveyParameter

  final case object older extends SurveyParameter
}
