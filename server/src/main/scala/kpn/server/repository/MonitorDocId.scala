package kpn.server.repository

import kpn.api.common.changes.details.ChangeKey
import kpn.core.db.KeyPrefix

object MonitorDocId {

  def routeDocId(routeId: Long): String = {
    s"${KeyPrefix.MonitorRoute}:$routeId"
  }

  def routeStateDocId(routeId: Long): String = {
    s"${KeyPrefix.MonitorRouteState}:$routeId"
  }

  def routeReferenceDocId(routeId: Long, key: String): String = {
    s"${KeyPrefix.MonitorRouteReference}:$routeId:$key"
  }

  def routeChangeDocId(key: ChangeKey): String = {
    routeChangeDocId(key.elementId, key.changeSetId, key.replicationNumber)
  }

  def routeChangeDocId(routeId: Long, changeSetId: Long, replicationNumber: Long): String = {
    s"${KeyPrefix.MonitorRouteChange}:$routeId:$changeSetId:$replicationNumber"
  }

  def routeChangeGeometryDocId(key: ChangeKey): String = {
    routeChangeGeometryDocId(key.elementId, key.changeSetId, key.replicationNumber)
  }

  def routeChangeGeometryDocId(routeId: Long, changeSetId: Long, replicationNumber: Long): String = {
    s"${KeyPrefix.MonitorRouteChangeGeometry}:$routeId:$changeSetId:$replicationNumber}"
  }

}
