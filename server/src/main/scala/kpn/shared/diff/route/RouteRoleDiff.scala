package kpn.shared.diff.route

case class RouteRoleDiff(before: Option[String], after: Option[String]) {
  def isAdd: Boolean = before.isEmpty && after.nonEmpty
  def isDelete: Boolean = before.nonEmpty && after.isEmpty
  def isUpdate: Boolean = before.nonEmpty && after.nonEmpty && before.get != after.get
}
