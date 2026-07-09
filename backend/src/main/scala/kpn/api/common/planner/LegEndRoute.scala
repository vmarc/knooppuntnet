package kpn.api.common.planner

import kpn.api.common.common.TrackPathKey
import kpn.core.doc.Storable

case class LegEndRoute(trackPathKeys: Seq[TrackPathKey], selection: Option[TrackPathKey]) extends Storable
