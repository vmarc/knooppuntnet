package kpn.core.poi

import kpn.api.common.LatLon

trait PoiLocationFilter {

  /*
    Returns true if given a point-of-interest at given location should be
    included in the database.
  */
  def filter(latLon: LatLon): Boolean

}
