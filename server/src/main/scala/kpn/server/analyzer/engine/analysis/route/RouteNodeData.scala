package kpn.server.analyzer.engine.analysis.route

case class RouteNodeData(
  nodeId: Long,
  //  latitude: String,
  //  longitude: String,
  name: String,
  alternateName: String,
  //  longName: Option[String] = None,
  //  definedInRelation: Boolean = false,
  //  definedInWay: Boolean = false,
  isInWay: Boolean,
)
