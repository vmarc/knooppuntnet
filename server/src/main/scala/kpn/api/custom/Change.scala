package kpn.api.custom

import kpn.api.common.changes.ChangeAction.Create
import kpn.api.common.changes.ChangeAction.Delete
import kpn.api.common.changes.ChangeAction.Modify
import kpn.api.common.data.raw.RawElement

object Change {

  def create(element: RawElement): Change = Change(Create, Seq(element))

  def modify(element: RawElement): Change = Change(Modify, Seq(element))

  def delete(element: RawElement): Change = Change(Delete, Seq(element))
}

case class Change(action: Int, elements: Seq[RawElement])
