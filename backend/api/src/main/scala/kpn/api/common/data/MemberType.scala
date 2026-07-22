package kpn.api.common.data

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MemberType extends EnumEntry with Hyphencase

object MemberType extends Enum[MemberType] {

  val values: IndexedSeq[MemberType] = findValues

  case object Node extends MemberType

  case object Way extends MemberType

  case object Relation extends MemberType
}

