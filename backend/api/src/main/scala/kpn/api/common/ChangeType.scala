package kpn.api.common

import enumeratum.Enum
import enumeratum.EnumEntry
import enumeratum.EnumEntry.Hyphencase

sealed trait ChangeType extends EnumEntry with Hyphencase

object ChangeType extends Enum[ChangeType] {

  val values: IndexedSeq[ChangeType] = findValues

  /*
    The change represents the oldest known situation of the element (if the element existed at that time).
   */
  case object InitialValue extends ChangeType

  /*
    The element was added in this changeset. There is an 'after' situation but no 'before'.
   */
  case object Create extends ChangeType

  /*
    The element was updated in this changeset. There is both a 'before' and an 'after' situation.
   */
  case object Update extends ChangeType

  /*
    The element was deleted in this changeset. There is a 'before' situation but no 'after'. If the element
    looses the required tags to be a network or route relation or network node, then the ChangeType is 'Update'
    rather than 'Delete'.
   */
  case object Delete extends ChangeType
}
