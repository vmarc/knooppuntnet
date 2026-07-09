package kpn.api.common.location

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait SurveyParameter extends EnumEntry with Hyphencase

object SurveyParameter extends Enum[SurveyParameter] {

  val values: IndexedSeq[SurveyParameter] = findValues

  final case object Unknown extends SurveyParameter

  final case object LastMonth extends SurveyParameter

  final case object LastHalfYear extends SurveyParameter

  final case object LastYear extends SurveyParameter

  final case object LastTwoYears extends SurveyParameter

  final case object Older extends SurveyParameter
}
