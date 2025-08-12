package kpn.core.tools.monitor.support

import kpn.api.common.monitor.MonitorRouteSegment
import kpn.core.doc.Storable

// TODO scala3 move back into using class
case class OsmSegments(
  osmSegments: Seq[MonitorRouteSegment],
) extends Storable
