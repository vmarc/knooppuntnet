package kpn.api.common.route

import kpn.api.common.LatLon

case class RouteNode(
  nodeId: Long,
  latitude: String,
  longitude: String,
  name: String,
  alternateName: String,
  //  longName: Option[String] = None,
  //  definedInRelation: Boolean = false,
  //  definedInWay: Boolean = false,
  isInWay: Boolean,
) extends LatLon
