package kpn.api.common.diff

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait TagDiffType extends EnumEntry with Hyphencase

object TagDiffType extends Enum[TagDiffType] {

  val values: IndexedSeq[TagDiffType] = findValues

  final case object same extends TagDiffType

  final case object delete extends TagDiffType

  final case object update extends TagDiffType

  final case object add extends TagDiffType
}

