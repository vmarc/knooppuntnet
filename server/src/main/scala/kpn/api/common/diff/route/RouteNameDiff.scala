package kpn.api.common.diff.route

import kpn.api.common.common.ToStringBuilder

case class RouteNameDiff(before: String, after: String) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("before", before).
    field("after", after).
    build
}
