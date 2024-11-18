package kpn.server.search

import kpn.api.common.GeocoderLocation

trait Geocoder {
  def search(query: String): Seq[GeocoderLocation]
}
