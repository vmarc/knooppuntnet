package kpn.core.doc

import kpn.api.base.WithStringId

case class LocationDoc(
  _id: String,
  parents: Seq[String],
  name: String,
  names: Seq[LocationName]
) extends WithStringId
