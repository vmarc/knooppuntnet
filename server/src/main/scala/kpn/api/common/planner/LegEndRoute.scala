package kpn.api.common.planner

import kpn.api.common.common.TrackPathKey

case class LegEndRoute(trackPathKeys: Seq[TrackPathKey], selection: Option[TrackPathKey]) {
}
