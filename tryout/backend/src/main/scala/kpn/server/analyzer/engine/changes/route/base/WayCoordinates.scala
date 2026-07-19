package kpn.server.analyzer.engine.changes.route.base

import kpn.server.domain.StringCoordinate

case class WayCoordinates(wayId: Long, coordinates: Seq[StringCoordinate])
