package kpn.api.common.changes

object ChangeAction extends Enumeration {
  type ChangeAction = Value
  val Create, Modify, Delete = Value
}
