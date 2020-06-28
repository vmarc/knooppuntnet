package kpn.api.common.common

case class TrackPathKey(routeId: Long, pathId: Long) {

  def key: String = routeId.toString + "+" + pathId.toString

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("routeId", routeId).
    field("pathId", pathId).
    build
}
