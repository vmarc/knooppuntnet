package kpn.core.doc

import kpn.api.base.WithStringId

case class LocationDoc(
  _id: String,
  paths: Seq[LocationPath],
  name: String,
  names: Seq[LocationName]
) extends WithStringId {

  def matchesPath(path: LocationPath): Boolean = {
    if (path.locationIds.nonEmpty) {
      paths.contains(path)
    }
    else {
      paths.isEmpty
    }
  }
}
