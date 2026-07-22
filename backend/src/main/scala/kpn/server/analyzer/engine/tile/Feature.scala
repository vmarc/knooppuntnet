package kpn.server.analyzer.engine.tile

import kpn.api.common.FeatureLayer
import org.locationtech.jts.geom.Geometry

case class Feature(
  layer: FeatureLayer,
  attributes: Map[String, ?],
  geometry: Geometry
)
