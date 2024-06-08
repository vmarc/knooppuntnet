package kpn.core.tools.location

import kpn.api.custom.Tags
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath

object LocationData {

  def apply(
    id: String,
    relationId: Long,
    name: String,
    names: Seq[LocationName],
    tags: Tags,
    geometry: LocationGeometry
  ): LocationData = {
    val paths: Seq[LocationPath] = Seq.empty
    LocationData(
      id,
      relationId,
      paths,
      name,
      names,
      tags,
      geometry
    )
  }

  def from(
    id: String,
    relationId: Long,
    paths: Seq[String],
    name: String,
    names: Seq[LocationName],
    tags: Tags,
    geometry: LocationGeometry
  ): LocationData = {
    LocationData(
      id,
      relationId,
      Seq(LocationPath(paths)),
      name,
      names,
      tags,
      geometry
    )
  }
}

case class LocationData(
  id: String,
  relationId: Long,
  paths: Seq[LocationPath],
  name: String,
  names: Seq[LocationName],
  tags: Tags,
  geometry: LocationGeometry
) {

  def toLocationNameDefinition: LocationNameDefinition = {
    val namesOption = if (names.nonEmpty) Some(names) else None
    LocationNameDefinition(
      id,
      relationId,
      paths,
      name,
      namesOption,
      tags
    )
  }

  def contains(otherLocationGeometry: LocationGeometry): Boolean = {
    geometry.contains(otherLocationGeometry)
  }
}
