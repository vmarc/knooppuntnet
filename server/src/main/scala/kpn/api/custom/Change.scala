package kpn.api.custom

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.raw.RawElement
import kpn.api.common.data.raw.RawNode
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.data.raw.RawWay

case class Change(
  action: ChangeAction,
  nodes: Seq[RawNode],
  ways: Seq[RawWay],
  relations: Seq[RawRelation],
) {
  def elements: Seq[RawElement] = {
    nodes ++ ways ++ relations
  }
}
