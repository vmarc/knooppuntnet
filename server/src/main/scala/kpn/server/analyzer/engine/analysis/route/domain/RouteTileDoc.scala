package kpn.server.analyzer.engine.analysis.route.domain

import kpn.api.base.WithStringId
import kpn.api.custom.NetworkType

case class RouteTileDoc(
  _id: String,
  routeId: Long,
  routeName: String,
  tile: String,
  scope: String,
  networkTypes: Seq[NetworkType],
  geometries: Seq[String]
) extends WithStringId
