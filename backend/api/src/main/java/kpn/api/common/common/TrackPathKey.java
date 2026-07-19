package kpn.api.common.common;

public record TrackPathKey(
  Long routeId,
  Long pathId
) {
}

/*
package kpn.api.common.common

case class TrackPathKey(routeId: Long, pathId: Long) {

  def key: String = s"${routeId.toString}.${pathId.toString}"

}

*/
