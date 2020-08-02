package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPathKey

case class LegEndRoute(trackPathKeys: Seq[TrackPathKey], selection: Option[TrackPathKey]) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("trackPathKeys", trackPathKeys).
    build

}
