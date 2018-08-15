package kpn.client.common.map.vector

trait SelectedFeature

case class SelectedNode(nodeId: Long, name: String) extends SelectedFeature
case class SelectedRoute(routeId: Long, name: String) extends SelectedFeature
