package kpn.core.doc

import kpn.api.base.WithStringId
import org.locationtech.jts.geom.Geometry

case class LocationGeometryDoc(
  _id: String,
  geometry: Geometry
) extends WithStringId
