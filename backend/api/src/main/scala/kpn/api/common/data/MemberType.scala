package kpn.api.common.data

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait MemberType extends EnumEntry with Hyphencase

object MemberType extends Enum[MemberType] {

  val values: IndexedSeq[MemberType] = findValues

  final case object Node extends MemberType

  final case object Way extends MemberType

  final case object Relation extends MemberType
}

