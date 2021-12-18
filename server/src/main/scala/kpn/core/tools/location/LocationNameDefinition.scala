package kpn.core.tools.location

import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath

case class LocationNameDefinition(
  id: String,
  paths: Seq[LocationPath],
  name: String,
  names: Option[Seq[LocationName]]
)
