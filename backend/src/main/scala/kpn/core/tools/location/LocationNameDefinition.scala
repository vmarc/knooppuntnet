package kpn.core.tools.location

import kpn.api.custom.Tag
import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath
import kpn.core.doc.Storable

case class LocationNameDefinition(
  id: String,
  relationId: Long,
  paths: Seq[LocationPath],
  name: String,
  names: Option[Seq[LocationName]],
  tags: Seq[Tag]
) extends Storable
