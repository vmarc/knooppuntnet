package kpn.core.tools.location

import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import org.locationtech.jts.geom.Geometry

object LocationData {

  def apply(
    id: String,
    name: String,
    names: Seq[LocationName],
    geometry: LocationGeometry
  ): LocationData = {
    val paths: Seq[LocationPath] = Seq.empty
    LocationData(
      id,
      paths,
      name,
      names,
      geometry
    )
  }

  def from(
    id: String,
    paths: Seq[String],
    name: String,
    names: Seq[LocationName],
    geometry: LocationGeometry

  ): LocationData = {
    LocationData(
      id,
      Seq(LocationPath(paths)),
      name,
      names,
      geometry
    )
  }
}

case class LocationData(
  id: String,
  paths: Seq[LocationPath],
  name: String,
  names: Seq[LocationName],
  geometry: LocationGeometry
) {

  def toLocationNameDefinition: LocationNameDefinition = {
    val namesOption = if (names.nonEmpty) Some(names) else None
    LocationNameDefinition(
      id,
      paths,
      name,
      namesOption
    )
  }

  def contains(otherGeometry: Geometry): Boolean = {
    geometry.geometry.contains(otherGeometry)
  }

  def contains(otherLocationGeometry: LocationGeometry): Boolean = {
    geometry.geometry.contains(otherLocationGeometry.geometry)
  }
}
