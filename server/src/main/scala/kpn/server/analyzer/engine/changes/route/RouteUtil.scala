package kpn.server.analyzer.engine.changes.route

import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.RouteDetailAnalysis

object RouteUtil {

  private val log = Log(classOf[RouteUtil])

  def assertVersion1(routeAnalysis: RouteDetailAnalysis): Unit = {
    val version = routeAnalysis.routeDetail.version
    if (version != 1) {
      val message = s"Route '${routeAnalysis.id}' was not found in the database at the time before the changeset; " +
        s"we would expect the version of the node to be 1, but we found $version. " +
        "Continued processing anyway."
      log.warn(message)
    }
  }
}

class RouteUtil
