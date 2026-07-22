package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait AnalysisStrategy extends EnumEntry with Hyphencase

object AnalysisStrategy extends Enum[AnalysisStrategy] {

  val values: IndexedSeq[AnalysisStrategy] = findValues

  case object Location extends AnalysisStrategy

  case object Network extends AnalysisStrategy
}
