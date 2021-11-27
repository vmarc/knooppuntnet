package kpn.core.tools.location

import org.locationtech.jts.geom.Geometry

case class LocationGeometryDoc(
  _id: String,
  geometry: Geometry
)
