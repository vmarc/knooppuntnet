package kpn.core.planner.graph

import kpn.api.common.common.ToStringBuilder

case class GraphPath(source: String, segments: Seq[GraphPathSegment]) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("source", source).
    field("segments", segments).
    build

}
