package kpn.server.analyzer.engine.changes.route.base

import kpn.server.analyzer.engine.changes.ChangeSetContext

class BaseRouteChangeDeleterMock extends BaseRouteChangeDeleter {
  var deletedRouteIds: Seq[Long] = Seq.empty

  override def delete(changeSetContext: ChangeSetContext, routeId: Long): ChangeSetContext = {
    deletedRouteIds = deletedRouteIds :+ routeId
    changeSetContext
  }
}
