package kpn.core.tools.location

import kpn.api.custom.Tags
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath

case class LocationNameDefinition(
  id: String,
  relationId: Long,
  paths: Seq[LocationPath],
  name: String,
  names: Option[Seq[LocationName]],
  tags: Tags
)
