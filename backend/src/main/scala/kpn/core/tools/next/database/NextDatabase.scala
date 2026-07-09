package kpn.core.tools.next.database

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.NextRouteState
import kpn.database.base.DatabaseCollection

class NextDatabase(val database: MongoDatabase) {

  def getCollection[T](collectionName: String): MongoCollection[T] = {
    database.getCollection(collectionName).asInstanceOf[MongoCollection[T]]
  }

  def routeRelations: DatabaseCollection[NextRouteRelation] = {
    new DatabaseCollection(database.getCollection("route-relations", classOf[NextRouteRelation]))
  }

  def routeStates: DatabaseCollection[NextRouteState] = {
    new DatabaseCollection(database.getCollection("route-states", classOf[NextRouteState]))
  }
}
