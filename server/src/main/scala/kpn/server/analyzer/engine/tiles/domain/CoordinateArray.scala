package kpn.server.analyzer.engine.tiles.domain

import kpn.core.doc.Storable
import org.locationtech.jts.geom.Coordinate

case class CoordinateArray(coordinates: Array[Coordinate]) extends Storable
