package kpn.core.doc

case class LocationPath(locationIds: Seq[String]) {
  def contains(path: LocationPath): Boolean = {
    if (path.locationIds.nonEmpty && path.locationIds.size <= locationIds.size) {
      path.locationIds.zip(locationIds).forall { case (l1, l2) => l1 == l2 }
    }
    else {
      false
    }
  }
}
