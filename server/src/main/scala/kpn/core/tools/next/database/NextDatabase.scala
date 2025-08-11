package kpn.core.tools.next.database

import com.mongodb.client.MongoCollection
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.NextRouteState
import kpn.database.base.DatabaseCollection

trait NextDatabase {

  def getCollection[T](collectionName: String): MongoCollection[T]

  def routeRelations: DatabaseCollection[NextRouteRelation]

  def routeStates: DatabaseCollection[NextRouteState]
}
