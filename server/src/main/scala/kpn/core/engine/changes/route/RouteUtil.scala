package kpn.core.engine.changes.route

import kpn.core.engine.analysis.route.RouteAnalysis
import kpn.core.util.Log

object RouteUtil {

  private val log = Log(classOf[RouteUtil])

  def assertVersion1(routeAnalysis: RouteAnalysis): Unit = {
    val version = routeAnalysis.route.version
    if (version != 1) {
      val message = s"Route '${routeAnalysis.id}' was not found in the database at the time before the changeset; " +
        s"we would expect the version of the node to be 1, but we found $version. " +
        "Continued processing anyway."
      log.warn(message)
    }
  }
}

class RouteUtil
