package kpn.api.common.planner

import kpn.api.common.common.TrackPathKey
import kpn.api.id.Storable

case class LegEndRoute(trackPathKeys: Seq[TrackPathKey], selection: Option[TrackPathKey]) extends Storable
