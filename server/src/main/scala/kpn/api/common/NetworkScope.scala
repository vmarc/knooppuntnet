package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry

sealed trait NetworkScope extends EnumEntry

object NetworkScope extends Enum[NetworkScope] {

  val values: IndexedSeq[NetworkScope] = findValues

  final case object local extends NetworkScope
  final case object regional extends NetworkScope
  final case object national extends NetworkScope
  final case object international extends NetworkScope
}
