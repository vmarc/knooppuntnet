package kpn.server.analyzer.engine.changes.route

import kpn.api.common.diff.RouteData
import kpn.core.util.Log

object RouteUtil {

  private val log = Log(classOf[RouteUtil])

  def assertVersion1(routeData: RouteData): Unit = {
    val version = routeData.meta.version
    if (version != 1) {
      val message = s"Route '${routeData.relationId}' was not found in the database at the time before the changeset; " +
        s"we would expect the version of the node to be 1, but we found $version. " +
        "Continued processing anyway."
      log.warn(message)
    }
  }
}

class RouteUtil
