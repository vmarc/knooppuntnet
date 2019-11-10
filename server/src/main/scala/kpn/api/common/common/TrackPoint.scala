package kpn.api.common.common

case class TrackPoint(lat: String, lon: String) {
  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("lat", lat).
    field("lon", lon).
    build
}
