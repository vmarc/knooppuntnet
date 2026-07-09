package kpn.api.common

case class NodeIntegrityCheckChange(routeType: RouteType, before: Option[NodeIntegrityCheck], after: Option[NodeIntegrityCheck])
