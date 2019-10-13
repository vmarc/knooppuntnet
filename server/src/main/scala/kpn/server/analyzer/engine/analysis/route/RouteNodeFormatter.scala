package kpn.server.analyzer.engine.analysis.route

class RouteNodeFormatter(routeNode: RouteNode) {

  def shortString: String = "%s/%s".format(routeNode.node.id, routeNode.alternateName)

  def longString: String = "%s/%s/%s/%s/%s%s".format(
    routeNode.nodeType.toString,
    routeNode.node.id,
    routeNode.name,
    routeNode.alternateName,
    if (routeNode.definedInRelation) "R" else "",
    if (routeNode.definedInWay) "W" else ""
  )
}
