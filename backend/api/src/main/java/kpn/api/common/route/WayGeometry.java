package kpn.api.common.route;

import kpn.api.common.route.WayLine;

public record WayGeometry(
  Long wayId,
  WayLine line
) {
}

/*
package kpn.api.common.route

case class WayGeometry(wayId: Long, line: WayLine)

*/
