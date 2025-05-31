package kpn.server.analyzer.engine.tile

import org.locationtech.jts.geom.Geometry

case class Feature(
  layerName: String,
  attributes: Map[String, _],
  geometry: Geometry
)
