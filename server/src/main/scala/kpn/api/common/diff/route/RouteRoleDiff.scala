package kpn.api.common.diff.route

import kpn.api.common.common.ToStringBuilder

case class RouteRoleDiff(before: Option[String], after: Option[String]) {

  def isAdd: Boolean = before.isEmpty && after.nonEmpty

  def isDelete: Boolean = before.nonEmpty && after.isEmpty

  def isUpdate: Boolean = before.nonEmpty && after.nonEmpty && before.get != after.get

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("before", before).
    field("after", after).
    build
}
