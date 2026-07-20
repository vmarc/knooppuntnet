package kpn.api.common.route;

public record WayLine(
  Long nodeCount,
  Long meters,
  String line
) {}

/* TODO migrate

import kpn.api.common.LatLon
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.changes.route.base.WayCoordinates
import kpn.server.analyzer.engine.tiles.domain.CoordinateCodec
import org.locationtech.jts.geom.Coordinate

object WayLine {

  def fromLatLons(latLons: Seq[LatLon]): WayLine = {
    val coordinates = latLons.toArray.map(latLon => new Coordinate(latLon.lat, latLon.lon))
    val nodeCount = coordinates.length
    val meters = Math.round(Haversine.meters(coordinates))
    val line = CoordinateCodec.encode(coordinates.toArray)
    WayLine(nodeCount, meters, line)
  }

  def from(wayCoordinates: WayCoordinates): WayLine = {
    val nodeCount = wayCoordinates.coordinates.length
    val meters = Haversine.meters(wayCoordinates.coordinates)
    val line = CoordinateCodec.encodeStringCoordinates(wayCoordinates.coordinates.toArray)
    WayLine(nodeCount, meters, line)
  }
}

*/
