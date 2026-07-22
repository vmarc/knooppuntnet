package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait SurveyParameter extends EnumEntry with Hyphencase

object SurveyParameter extends Enum[SurveyParameter] {

  val values: IndexedSeq[SurveyParameter] = findValues

  case object Unknown extends SurveyParameter

  case object LastMonth extends SurveyParameter

  case object LastHalfYear extends SurveyParameter

  case object LastYear extends SurveyParameter

  case object LastTwoYears extends SurveyParameter

  case object Older extends SurveyParameter
}
