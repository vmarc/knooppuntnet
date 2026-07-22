package kpn.api.common.diff

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait TagDiffType extends EnumEntry with Hyphencase

object TagDiffType extends Enum[TagDiffType] {

  val values: IndexedSeq[TagDiffType] = findValues

  case object same extends TagDiffType

  case object delete extends TagDiffType

  case object update extends TagDiffType

  case object add extends TagDiffType
}

