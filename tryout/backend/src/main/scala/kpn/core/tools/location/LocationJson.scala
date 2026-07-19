package kpn.core.tools.location

import org.locationtech.jts.geom.Geometry

case class LocationJsonProperties(id: String, osm_id: Long, name: String, all_tags: Map[String, String])

case class LocationJson(properties: LocationJsonProperties, bbox: Seq[String], geometry: Geometry)

case class LocationsJson(features: Seq[LocationJson])
