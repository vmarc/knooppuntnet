package kpn.api.custom

import kpn.api.common.changes.ChangeAction.ChangeAction
import kpn.api.common.data.raw.RawElement

case class Change(action: ChangeAction, elements: Seq[RawElement])
