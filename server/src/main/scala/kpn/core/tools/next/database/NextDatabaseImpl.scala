package kpn.core.tools.next.database

import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import kpn.core.tools.next.domain.NextRouteRelation
import kpn.core.tools.next.domain.NextRouteState
import kpn.database.base.DatabaseCollection
import kpn.database.base.DatabaseCollectionImpl

import scala.reflect.ClassTag

class NextDatabaseImpl(val database: MongoDatabase) extends NextDatabase {

  override def getCollection[T: ClassTag](collectionName: String): MongoCollection[T] = {
    database.getCollection(collectionName).asInstanceOf[MongoCollection[T]]
  }

  override def routeRelations: DatabaseCollection[NextRouteRelation] = {
    new DatabaseCollectionImpl(database.getCollection("route-relations", classOf[NextRouteRelation]))
  }

  override def routeStates: DatabaseCollection[NextRouteState] = {
    new DatabaseCollectionImpl(database.getCollection("route-states", classOf[NextRouteState]))
  }
}
