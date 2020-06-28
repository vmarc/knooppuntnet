package kpn.core.planner.graph

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPathKey

case class GraphPathSegment(sink: String, pathKey: TrackPathKey) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("sink", sink).
    field("pathKey", pathKey).
    build

}
