package kpn.shared.changes

import kpn.shared.changes.ChangeAction.Create
import kpn.shared.changes.ChangeAction.Delete
import kpn.shared.changes.ChangeAction.Modify
import kpn.shared.data.raw.RawElement

object Change {

  def create(element: RawElement): Change = Change(Create, Seq(element))

  def modify(element: RawElement): Change = Change(Modify, Seq(element))

  def delete(element: RawElement): Change = Change(Delete, Seq(element))
}

case class Change(action: Int, elements: Seq[RawElement])
