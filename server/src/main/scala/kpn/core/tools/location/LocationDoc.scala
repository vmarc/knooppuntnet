package kpn.core.tools.location

import kpn.core.doc.LocationName
import kpn.core.doc.LocationPath

case class LocationDoc(
  _id: String,
  paths: Seq[LocationPath],
  name: String,
  names: Seq[LocationName]
) {

  def matchesPath(path: LocationPath): Boolean = {
    if (path.locationIds.nonEmpty) {
      paths.contains(path)
    }
    else {
      paths.isEmpty
    }
  }
}
